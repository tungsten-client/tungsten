package org.tungsten.client.languageserver;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ClassTreeBuilder {
	public record Member(Type type, String name, String desc, int mods, String owner) {
		public enum Type {
			METHOD, FIELD
		}

		@Override
		public String toString() {
			return String.format("%d %s %s.%s %s", mods, type, owner, name, desc);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Member member = (Member) o;

			if (mods != member.mods) return false;
			if (type != member.type) return false;
			if (!Objects.equals(name, member.name)) return false;
			if (!Objects.equals(desc, member.desc)) return false;
			return Objects.equals(owner, member.owner);
		}

		@Override
		public int hashCode() {
			int result = type != null ? type.hashCode() : 0;
			result = 31 * result + (name != null ? name.hashCode() : 0);
			result = 31 * result + (desc != null ? desc.hashCode() : 0);
			result = 31 * result + mods;
			result = 31 * result + (owner != null ? owner.hashCode() : 0);
			return result;
		}
	}
	public record Class(String name, String superName, Member[] members) {
		@Override
		public String toString() {
			StringBuilder t = new StringBuilder(name + " extends " + superName + "\n");
			for (Member member : members) {
				t.append("  ").append(member.toString()).append("\n");
			}
			return t.toString().trim();
		}

		public String getSimpleName() {
			String[] tokn = name.split("/");
			return tokn[tokn.length - 1];
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			Class aClass = (Class) o;

			if (!Objects.equals(name, aClass.name)) return false;
			if (!Objects.equals(superName, aClass.superName)) return false;
			// Probably incorrect - comparing Object[] arrays with Arrays.equals
			return Arrays.equals(members, aClass.members);
		}

		@Override
		public int hashCode() {
			int result = name != null ? name.hashCode() : 0;
			result = 31 * result + (superName != null ? superName.hashCode() : 0);
			result = 31 * result + Arrays.hashCode(members);
			return result;
		}
	}
	private interface Resolver {
		ClassNode resolve(String name) throws IOException;
		String[] list() throws IOException;
	}
	private static class JarFileResolver implements Resolver {
		private final FileSystem jfs;

		public JarFileResolver(Path p) throws IOException {
			jfs = FileSystems.newFileSystem(p);
		}
		@Override
		public ClassNode resolve(String name) throws IOException {
			Path p = jfs.getPath(name+".class");
			if (!Files.isRegularFile(p)) return null;
			try (InputStream inputStream = Files.newInputStream(p)) {
				ClassNode c = new ClassNode();
				ClassReader cr = new ClassReader(inputStream);
				cr.accept(c, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
				return c;
			}
		}

		@Override
		public String[] list() throws IOException {
			return Files.walk(jfs.getPath("/"))
					.filter(f -> Files.isRegularFile(f) && f.getFileName().toString().endsWith(".class"))
					.map(p -> {
				String string = p.toString();
				return string.substring(1, string.length()-6);
			})
					.filter(f -> !f.endsWith("package-info") && !f.endsWith("module-info")).toArray(String[]::new);
		}
	}
	private static class JmodFileResolver implements Resolver {

		private final ModuleReader mr;
		private final String[] all;

		public JmodFileResolver(ModuleReference mod) {
			try (ModuleReader reader = mod.open()) {
				all = reader.list().filter(s -> s.endsWith(".class")).map(s -> s.substring(0, s.length() - 6)).toArray(String[]::new);
				mr = mod.open(); // second open, first open is consumed by .list(). stupid api but it works
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}
		@Override
		public ClassNode resolve(String name) throws IOException {
			Optional<InputStream> open = mr.open(name + ".class");
			if (open.isEmpty()) return null;
			try (InputStream inputStream = open.get()) {
				ClassNode c = new ClassNode();
				ClassReader cr = new ClassReader(inputStream);
				cr.accept(c, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
				return c;
			}
		}

		@Override
		public String[] list() {
			return all;
		}
	}

	/**
	 * Builds a class tree from the given jars
	 * @param sourceJarfile The primary jar. Indexed and listed
	 * @param complementaryJars Complementary jars (libraries). Optional, but will deliver better results
	 * @return A list of all classes in the primary jar
	 */
	public static Class[] buildTreeFrom(Path sourceJarfile, Path... complementaryJars) throws IOException {
		List<Resolver> allResolvers = new ArrayList<>();
		Resolver primary = new JarFileResolver(sourceJarfile);
		allResolvers.add(primary);
		allResolvers.addAll(Arrays.stream(complementaryJars).map(s -> {
			try {
				return new JarFileResolver(s);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}).toList());
		for (ModuleReference moduleReference : ModuleFinder.ofSystem().findAll()) {
			allResolvers.add(new JmodFileResolver(moduleReference));
		}
		List<Class> c = new ArrayList<>();
		for (String s : primary.list()) {
			ClassNode original = primary.resolve(s);
			assert original != null;
			List<Member> members = new ArrayList<>();
			collectMembersIn(original, members);
			ClassNode visiting = resolveFrom(allResolvers, original.superName);
			while (visiting != null) {
				collectMembersIn(visiting, members);
				visiting = resolveFrom(allResolvers, visiting.superName);
			}
			c.add(new Class(original.name, original.superName, members.toArray(Member[]::new)));
		}
		return c.toArray(Class[]::new);
	}

	private static ClassNode resolveFrom(List<Resolver> s, String ss) throws IOException {
		for (Resolver resolver : s) {
			ClassNode resolve = resolver.resolve(ss);
			if (resolve != null) return resolve;
		}
		return null;
	}

	private static void collectMembersIn(ClassNode cn, List<Member> out) {
		for (MethodNode method : cn.methods) {
			if (out.stream().noneMatch(c -> c.name.equals(method.name) && c.desc.equals(method.desc) && c.type == Member.Type.METHOD)) {
				out.add(new Member(Member.Type.METHOD, method.name, method.desc, method.access, cn.name));
			}
		}
		for (FieldNode field : cn.fields) {
			if (out.stream().noneMatch(c -> c.name.equals(field.name) && c.desc.equals(field.desc) && c.type == Member.Type.FIELD)) {
				out.add(new Member(Member.Type.FIELD, field.name, field.desc, field.access, cn.name));
			}
		}
	}

	public static void main(String[] args) throws Throwable {
		Class[] classes = buildTreeFrom(Path.of("/media/x150/Knowledge Base/Development/JavaObfMvn/core/target/original-core-1.0.jar"));
		for (Class aClass : classes) { System.out.println(aClass); }
	}
}

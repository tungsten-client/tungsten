package org.tungsten.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebUtils {
	static final HttpClient client = HttpClient.newHttpClient();

	public static void downloadURLToPath(String fileUrl, Path savePath) {
		HttpRequest req = HttpRequest.newBuilder()
				.uri(URI.create(fileUrl))
				.build();
		try {
			HttpResponse<InputStream> send = client.send(req, HttpResponse.BodyHandlers.ofInputStream());
			try (InputStream body = send.body(); OutputStream out = Files.newOutputStream(savePath)) {
				byte[] buffer = new byte[1024];
				int c;
				while ((c = body.read(buffer)) > 0) {
					out.write(buffer, 0, c);
				}
			}
		} catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

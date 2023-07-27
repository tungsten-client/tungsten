package org.tungsten.client.gui.clickgui;

import org.tungsten.client.Tungsten;
import org.tungsten.client.feature.module.GenericModule;
import org.tungsten.client.feature.module.ModuleType;
import org.tungsten.client.feature.module.ModuleTypeManager;
import org.tungsten.client.feature.module.config.*;
import org.tungsten.client.feature.registry.ModuleRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TungstenBridge {

	public void toggleModule(int moduleID) {
		ModuleRegistry.getByID(moduleID).toggle();
	}

	public boolean queryEnabled(int moduleID) {
		return ModuleRegistry.getByID(moduleID).isEnabled();
	}

	public void setWindowPosition(String windowHandle, double wind_x, double wind_y) {
		ModuleType m = ModuleTypeManager.getByName(windowHandle);
		m.setPos(wind_x, wind_y);
	}

	public double getWindowX(String windowHandle) {
		return ModuleTypeManager.getByName(windowHandle).getX();
	}

	public double getWindowY(String windowHandle) {
		return ModuleTypeManager.getByName(windowHandle).getY();
	}

	public String[] getModuleTypes() {
		return ModuleTypeManager.getModuleTypes().stream().map(ModuleType::getName).toArray(String[]::new);
	}

	public Integer[] getModulesByType(String moduleType) {
		List<Integer> modules = new ArrayList<Integer>();
		for (GenericModule mod : ModuleRegistry.modules) {
			if (mod.getType().equals(moduleType)) {
				modules.add(mod.getID());
			}
		}
		return modules.toArray(Integer[]::new);
	}

	public void print(String real) {
		Tungsten.LOGGER.info(real);
	}

	public String getName(int moduleID) { return ModuleRegistry.getByID(moduleID).getName(); }

	public String getDescription(int moduleID) {
		return ModuleRegistry.getByID(moduleID).getDescription();
	}

	public void broadcastTextboxUpdate(String name, int moduleID, String value) {
		GenericModule mod = ModuleRegistry.getByID(moduleID);
		if (mod != null) {
			GenericSetting<?> mms = mod.getSettingByName(name);
			if (mms instanceof TextboxSetting ts) {
				ts.setValue(value);
			}
		}
	}

	public void broadcastButtonClick(String name, int moduleID) {
		GenericModule mod = ModuleRegistry.getByID(moduleID);
		if (mod != null && mod.isEnabled()) {
			GenericSetting<?> mms = mod.getSettingByName(name);
			if (mms instanceof ButtonSetting bs) {
				bs.run();
			}
		}
	}

	public void broadcastSliderUpdate(String name, int moduleID, String value) {
		GenericModule mod = ModuleRegistry.getByID(moduleID);
		if (mod != null) {
			GenericSetting<?> mms = mod.getSettingByName(name);
			if (mms instanceof SliderSetting ss) {
				ss.setValue(Double.parseDouble(value));
			}
		}
	}

	public void broadcastCheckboxUpdate(String name, int moduleID, boolean value) {
		GenericModule mod = ModuleRegistry.getByID(moduleID);
		if (mod != null) {
			GenericSetting<?> mms = mod.getSettingByName(name);
			if (mms instanceof CheckboxSetting cs) {
				cs.setValue(value);
			}
		}
	}


	public String[] getSettingHTML(int moduleID) {
		Tungsten.LOGGER.info("REAL");
		Tungsten.LOGGER.info(moduleID + "");
		GenericModule xz = ModuleRegistry.getByID(moduleID);
		if (xz != null) {
			Tungsten.LOGGER.info("was not null");
			String[] real = xz.getSettings().stream().map(GenericSetting::toHTML).toArray(String[]::new);
			Tungsten.LOGGER.info(Arrays.toString(real));
			Tungsten.LOGGER.info("DONE");
			return real;
		} else {
			Tungsten.LOGGER.info("Was null");
			Tungsten.LOGGER.info("DONE");
			return null;
		}

	}


	public void updateKeybind(int moduleID, int keycode) {
		Tungsten.LOGGER.info("Updated keybind for " + moduleID + " to " + keycode);
		ModuleRegistry.getByID(moduleID).updateKeybind(keycode);
	}

	public int getKeybind(int moduleID) {
		return ModuleRegistry.getByID(moduleID).getKeybind();
	}

	public Integer[] getModulesByKeycode(int keycode) {
		return ModuleRegistry.modules.stream()
				.filter(mkc -> mkc.getKeybind() == keycode)
				.map(GenericModule::getID)
				.toArray(Integer[]::new);
	}

	public String[] getFilteredModulesByPartialName(String partialName) {
		if (partialName.equals("")) {
			return new String[]{}; //if it is the empty string we will return the empty set to prevent a crashing, may remove later
		}
		return ModuleRegistry.modules.stream()
				.map(GenericModule::getName)
				.filter(name -> name.toLowerCase().contains(partialName.toLowerCase()))
				.toArray(String[]::new);
	}


}

package org.sephire.games.framework4x.core.plugins;


import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of={"pluginName"})
public class PluginSpec {
	private String pluginName;
	private String rootPackage;
	private Option<String> parentPlugin;

	/**
	 * Check whether the plugin is a base plugin or not.
	 * It boils down to whether it has a defined parent plugin.
	 * @return
	 */
	public boolean isBasePlugin(){
		return parentPlugin.isEmpty();
	}


}

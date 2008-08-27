package jp.go.aist.rtm.rtclink.ui.workbenchadapter;

import jp.go.aist.rtm.rtclink.model.component.Port;
import jp.go.aist.rtm.rtclink.model.component.PortProfile;

/**
 * Port��WorkbenchAdapter
 */
public abstract class PortWorkbenchAdapter extends ModelElementWorkbenchAdapter {
	@Override
	public Object[] getChildren(Object o) {
		Object[] result = new Object[0];

		PortProfile portProfile = ((Port) o).getPortProfile();
		if (portProfile != null) {
			result = portProfile.getIterfaces().toArray();
		}

		return result;
	}
}

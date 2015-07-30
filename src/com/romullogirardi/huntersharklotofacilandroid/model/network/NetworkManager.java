package com.romullogirardi.huntersharklotofacilandroid.model.network;

import android.annotation.SuppressLint;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

public class NetworkManager {

	//IMPLEMENTAÇÃO COMO SINGLETON
	private static NetworkManager instance = null;

	public static NetworkManager getInstance() {
		if (instance == null) {
			instance = new NetworkManager();
		}
		return instance;
	}

	//GETTERS E SETTERS
	public String getMyIP() {
		return getIPAddress(true);
	}

	//MÉTODO DE OBTENÇÃO DINÂMICA DO ENDEREÇO IP
	@SuppressLint("DefaultLocale")
	public String getIPAddress(boolean useIPv4) {
		try {
			List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : interfaces) {
				List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
				for (InetAddress addr : addrs) {
					if (!addr.isLoopbackAddress()) {
						String sAddr = addr.getHostAddress().toUpperCase();
						boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
						if (useIPv4) {
							if (isIPv4) return sAddr;
						}
						else {
							if (!isIPv4) {
								int delim = sAddr.indexOf('%'); //tratar sufixo de porta IPv6
								return delim < 0 ? sAddr : sAddr.substring(0, delim);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {} 
		return "";
	}
}
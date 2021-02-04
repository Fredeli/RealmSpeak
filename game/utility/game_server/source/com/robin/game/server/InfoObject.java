/* 
 * RealmSpeak is the Java application for playing the board game Magic Realm.
 * Copyright (c) 2005-2015 Robin Warren
 * E-mail: robin@dewkid.com
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 *
 * http://www.gnu.org/licenses/
 */
package com.robin.game.server;

import java.util.ArrayList;

public class InfoObject {
	private boolean forHost;
	private String destClientName;
	private ArrayList<String> info; // list of Strings

	public InfoObject(String destClientName, ArrayList<String> info) {
		this.destClientName = destClientName;
		this.info = new ArrayList<>(info);
		this.forHost = destClientName==null;
	}
	public boolean isForHost() {
		return forHost;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("destClientName=");
		sb.append(destClientName);
		sb.append(", info=");
		sb.append(info);
		return sb.toString();
	}

	public String getDestClientName() {
		return destClientName;
	}

	public ArrayList<String> getInfo() {
		return info;
	}
}
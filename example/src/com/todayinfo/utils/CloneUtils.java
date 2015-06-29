package com.todayinfo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 克隆工具类
 * @author 荣
 *
 */
public class CloneUtils {

	/**
	 * 深度克隆对象
	 * 
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object copy(Serializable obj) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		return ois.readObject();
	}
}

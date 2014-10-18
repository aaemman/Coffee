package com.percolate.coffee.util.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by AlexanderEmmanuel on 2014-10-16.
 */
public class ImageUtils {

	public static Uri getImageUri(Context inContext, Bitmap inImage, String filename) {

		String path = Environment.getExternalStorageDirectory().toString();
		OutputStream fOut = null;
		File file = new File(path, filename);
		try {
			fOut = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		inImage.compress(Bitmap.CompressFormat.JPEG, 85, fOut);

		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return Uri.parse(path);
	}
}

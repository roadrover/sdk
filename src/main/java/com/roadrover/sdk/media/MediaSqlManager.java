package com.roadrover.sdk.media;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.roadrover.sdk.utils.Logcat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找文件数据库，从中获取数据 </br>
 * 从MediaService 里面读取音乐，视频等数据
 */
public class MediaSqlManager {
	/** content provider uri */
	private static final String URI_STRING = "content://com.roadrover.services/tbl_scanmedia";

	/**
	 * 类型定义列表
	 */
	public static final String[] MENU_TYPES = {"\'audio\'", "\'video\'", "\'book\'",
			"\'picture\'", "\'log\'", "\'apk\'"}; // 需要从数据库中查找的类型

	private Context mContext = null;
	
	public MediaSqlManager(@NonNull Context context) {
		if (context != null) {
			mContext = context.getApplicationContext();
		}
	}

	/**
	 * 检测这个ContentProvider uri是否合法
	 * 
	 * @param uri
	 * @return 合法返回true,否则返回false
	 */
	private boolean checkValidProvider(Uri uri) {
		if (null == mContext) {
			return false;
		}
		ContentProviderClient client = mContext.getContentResolver()
				.acquireContentProviderClient(uri);

		if (null == client) {
			return false;
		} else {
			client.release();
			return true;
		}
	}
	
	/**
	 * 查询音频文件
	 * @return 返回所有音频文件路径列表
	 */
	public List<String> queryAudioFile() {
		return query(IVIMedia.MediaSqlDataType.AUDIO_TYPE);
	}
	
	/**
	 * 查询视频文件
	 * @return 返回所有视频文件路径列表
	 */
	public List<String> queryVideoFile() {
		return query(IVIMedia.MediaSqlDataType.VIDEO_TYPE);
	}
	
	/**
	 * 查询电子书文件
	 * @return 返回所有电子书文件路径列表
	 */
	public List<String> queryBookFile() {
		return query(IVIMedia.MediaSqlDataType.BOOK_TYPE);
	}
	
	/**
	 * 查询垃圾文件 </br>
	 * 目前垃圾文件只包含 .log  .temp .tmp 等文件 </br>
	 * @return 返回所有垃圾文件路径列表
	 */
	public List<String> queryLogFile() {
		return query(IVIMedia.MediaSqlDataType.LOG_TYPE);
	}
	
	/**
	 * 查询apk文件
	 * @return 返回所有apk文件路径列表
	 */
	public List<String> queryApkFile() {
		return query(IVIMedia.MediaSqlDataType.APK_TYPE);
	}

	/**
	 * 查找图片文件
	 * @return 返回所有图片文件路径列表
     */
	public List<String> queryImageFile() { return query(IVIMedia.MediaSqlDataType.IMAGE_TYPE); }

	/**
	 * 从数据库中查询数据
	 * @param type 类型，{@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}
	 * @return 返回查询之后的结果
	 */
	public List<String> query(int type) {
		return query(type, "");
	}

	/**
	 * 查询指定路径，并且指令类型的文件
	 * @param type 类型，{@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}
	 * @param path 路径，指定目录下的数据
     * @return 返回查询到的文件路径列表
     */
	public List<String> query(int type, String path) {
		if (null == mContext) {
			return new ArrayList<>();
		}

		Uri uri = Uri.parse(URI_STRING);
		if (!checkValidProvider(uri))
			return getPhoneMusicList(type);

		if (type < 0 || type >= MENU_TYPES.length) {
			return new ArrayList<>();
		}

		String[] slSearchType = { "path" };
		String selection;
		String[] selectionArgs = null;
		if (TextUtils.isEmpty(path)) { // 路径为空
			selection = "type=?";
			selectionArgs = new String[]{MENU_TYPES[type].replace("\'", "")};
		} else { // 存在路径，用 like
			selection = "type=? and path like ?";
			selectionArgs = new String[]{MENU_TYPES[type].replace("\'", ""), path + "%"};
		}
		Cursor cursor = null;
		List<String> pathStrings = new ArrayList<>();
		try {
			cursor = mContext.getContentResolver().query(uri, slSearchType,
					selection, selectionArgs, null);

			if (null == cursor) {
				return getPhoneMusicList(type);
			}

			int nColumnIndex = cursor.getColumnIndex(slSearchType[0]);

			// 获取所有的文件的绝对路径
			while (cursor.moveToNext()) {
				pathStrings.add(cursor.getString(nColumnIndex));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}
		return pathStrings;
	}
	
	/**
	 * 将数据插入媒体数据库中
	 * @param path 路径
	 * @param name 名字
	 * @param type 类型 {@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}
	 */
	public void insertIntoMediaSql(String path, String name, int type) {
		if (null == mContext) return;
		
		Uri uri = Uri.parse(URI_STRING);
		if (!checkValidProvider(uri)) {
			Logcat.e("uri:" + uri + " no valid!");
			return;
		}
		if (type >= MENU_TYPES.length || type < 0) {
			Logcat.e("type length error:" + type);
			return;
		}
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("path", path);
		values.put("type", MENU_TYPES[type].replace("\'", ""));
		mContext.getContentResolver().insert(uri, values);
	}
	
	/**
	 * 获取手机的音乐列表
	 * @param type {@link com.roadrover.sdk.media.IVIMedia.MediaSqlDataType}
	 * @return 返回音乐文件列表
	 */
	private List<String> getPhoneMusicList(int type) {
		if (type == IVIMedia.MediaSqlDataType.AUDIO_TYPE) {
			String[] audioColumns = { MediaStore.Audio.Media._ID,
					MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.ARTIST,
					MediaStore.Audio.Media.MIME_TYPE, MediaStore.Audio.Media.SIZE,
					MediaStore.Audio.Media.DURATION, MediaStore.Audio.Media.ALBUM };
			Cursor cursor = null;
			List<String> audioList = new ArrayList<>();
			try {
				if (!checkValidProvider(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI)) {
					Logcat.e("uri:" + MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + " no valid!");
					return audioList;
				}
				cursor = managedQuery(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
						audioColumns, null, null, null);

				if (null != cursor && cursor.moveToFirst()) {
					do {
						String filePath = cursor.getString(cursor
								.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
						if ((new File(filePath)).length() < 100 * 1024) {
							// 小于100K，不加载
							continue;
						}
						audioList.add(filePath);
					} while (cursor.moveToNext());
				}
				System.gc();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (cursor != null) {
					cursor.close();
					cursor = null;
				}
			}
			return audioList;
		}
		return new ArrayList<String>();
	}
	
	private Cursor managedQuery(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (null == mContext) return null;
		return mContext.getContentResolver().query(uri, projection, selection,
				selectionArgs, sortOrder);
	}
}

package meizhi.meizhi.malin.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:2017/03/01 00:04
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public final class AssetsUtil {
    private AssetsUtil() {
    }

    public static String readFileFromAssets(Context context, String fileName) {
        //注意：如果资源文件是文本文件则需要考虑文件的编码和换行符。建议使用UTF-8和Unix换行符。
        String content = null;
        ByteArrayOutputStream output = null;
        InputStream input = null;
        try {
            if (context == null || TextUtils.isEmpty(fileName)) {
                return null;
            }
            AssetManager am = context.getApplicationContext().getAssets();
            input = am.open(fileName);
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            if (output != null) {
                content = new String(output.toByteArray(), "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return content;
    }

}

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 malin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package meizhi.meizhi.malin.utils;

import okhttp3.OkHttpClient;

/**
 * 类描述:OkHttpClient
 * 创建人:malin.myemail@163.com
 * 创建时间:on 16-10-13.
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public final class OkHttpClientUtil {


    private OkHttpClientUtil() {
    }

    private static volatile OkHttpClient mOkHttpClient;

    public static OkHttpClient getOkHttpClient() {
        return getOkHttpClientBuilder();
    }

    private static OkHttpClient getOkHttpClientBuilder() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpClientUtil.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = new OkHttpClient.Builder()
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }
}

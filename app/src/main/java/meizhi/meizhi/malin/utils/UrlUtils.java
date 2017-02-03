package meizhi.meizhi.malin.utils;

/**
 * 类描述:
 * 创建人:malin.myemail@163.com
 * 创建时间:17-2-2. 12:55
 * 备注:{@link }
 * 修改人:
 * 修改时间:
 * 修改备注:
 * 版本:
 */
public final class UrlUtils {

//    square.jpg      80x80
//    thumbnail.jpg   80x120
//    small.jpg 	  133x200
//    thumb150.jpg    150x150
//    thumb180 		  180x180
//    wap360.jpg 	  240x360
//    orj360.jpg      360x540
//    bmiddle.jpg 	  440x660
//    orj480.jpg      480x720
//    mw600.jpg       600x900
//    wap720.jpg      640x960
//    mw690.jpg       640x960
//    mw720.jpg       640x960
//    large.jpg 	  640x960


    public static final int square = 0;
    public static final int thumbnail = 1;
    public static final int small = 2;
    public static final int thumb150 = 3;
    public static final int thumb180 = 4;
    public static final int wap360 = 5;
    public static final int orj360 = 6;
    public static final int bmiddle = 7;
    public static final int orj480 = 8;
    public static final int mw600 = 9;
    public static final int wap720 = 10;
    public static final int mw690 = 11;
    public static final int mw720 = 12;
    public static final int mw1024 = 13;
    public static final int large = 14;

    public static final String square_ = "square";
    public static final String thumbnail_ = "thumbnail";
    public static final String small_ = "small";
    public static final String thumb150_ = "thumb150";
    public static final String thumb180_ = "thumb180";
    public static final String wap360_ = "wap360";
    public static final String orj360_ = "orj360";
    public static final String bmiddle_ = "bmiddle";
    public static final String orj480_ = "orj480";
    public static final String mw600_ = "mw600";
    public static final String wap720_ = "wap720";
    public static final String mw690_ = "mw690";
    public static final String mw720_ = "mw720";
    public static final String mw1024_ = "mw1024";
    public static final String large_ = "large";
    public static final String clouddn = "?imageView/2/h/500";


//    http://wwx.sinaimg.cn/thumbnail/xxx.jpg（缩略图）
//    http://wwx.sinaimg.cn/small/xxx.jpg （小图）
//    http://wwx.sinaimg.cn/bmiddle/xxx.jpg （中等图）
//    http://wwx.sinaimg.cn/large/xxx.jpg （最大的图）
    private UrlUtils() {

    }

    public static String getUrl(String url, int type) {
        String result;
        try {
            if (url.contains("sinaimg.cn")) {
                if (url.contains(large_)) {
                    result = url.replaceFirst(large_, getImageFlag(type));
                } else if (url.contains(mw690_)) {
                    result = url.replaceFirst(mw690_, getImageFlag(type));
                } else {
                    result = url;
                }
            } else if (url.contains("glb.clouddn.com") || url.contains("clouddn.com")) {
                result = url + clouddn;
            } else {
                result = url;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = url;
        }
        return result;
    }


    //http://7xi8d6.com1.z0.glb.clouddn.com/16123958_1630476787257847_7576387494862651392_n.jpg?imageView/2/h/500
    //http://ac-olwhhm4o.clouddn.com/4063qegYjlC8nx6uEqxV0kT3hn6hdqJqVWPKpdrS?imageView/2/h/500


    public static String getUrl2(String url, int type) {
        String result;
        try {
            if (url.contains("sinaimg.cn")) {

                if (url.contains(large_)) {
                    result = url.replaceFirst(large_, getImageFlag(type));
                } else if (url.contains(mw690_)) {
                    result = url.replaceFirst(mw690_, getImageFlag(type));
                } else if (url.contains(mw1024_)) {
                    result = url.replaceFirst(mw1024_, getImageFlag(type));
                } else if (url.contains(mw720_)) {
                    result = url.replaceFirst(mw720_, getImageFlag(type));
                } else if (url.contains(wap720_)) {
                    result = url.replaceFirst(wap720_, getImageFlag(type));
                } else if (url.contains(mw600_)) {
                    result = url.replaceFirst(mw600_, getImageFlag(type));
                } else if (url.contains(orj480_)) {
                    result = url.replaceFirst(orj480_, getImageFlag(type));
                } else if (url.contains(bmiddle_)) {
                    result = url.replaceFirst(bmiddle_, getImageFlag(type));
                } else if (url.contains(orj360_)) {
                    result = url.replaceFirst(orj360_, getImageFlag(type));
                } else if (url.contains(thumb180_)) {
                    result = url.replaceFirst(thumb180_, getImageFlag(type));
                } else if (url.contains(thumb150_)) {
                    result = url.replaceFirst(thumb150_, getImageFlag(type));
                } else if (url.contains(thumbnail_)) {
                    result = url.replaceFirst(thumbnail_, getImageFlag(type));
                } else if (url.contains(square_)) {
                    result = url.replaceFirst(square_, getImageFlag(type));
                } else {
                    result = url;
                }
            } else {
                result = url;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = url;
        }
        return result;
    }


    private static String getImageFlag(int type) {
        String flag = "";
        switch (type) {
            case square: {
                flag = square_;
                break;
            }

            case thumbnail: {
                flag = thumbnail_;
                break;
            }

            case small: {
                flag = small_;
                break;
            }

            case thumb150: {
                flag = thumb150_;
                break;
            }

            case thumb180: {
                flag = thumb180_;
                break;
            }


            case wap360: {
                flag = wap360_;
                break;
            }

            case orj360: {
                flag = orj360_;
                break;
            }

            case bmiddle: {
                flag = bmiddle_;
                break;
            }

            case orj480: {
                flag = orj480_;
                break;
            }
            case mw600: {
                flag = mw600_;
                break;
            }

            case wap720: {
                flag = wap720_;
                break;
            }

            case mw690: {
                flag = mw690_;
                break;
            }
            case mw720: {
                flag = mw720_;
                break;
            }
            case mw1024: {
                flag = mw1024_;
                break;
            }
            case large: {
                flag = large_;
                break;
            }
            default: {
                break;
            }
        }
        return flag;
    }
}

package zakjo.studentsapp.Helpers;


import java.text.SimpleDateFormat;
import java.util.Date;

public class GetTimeAgo {

    private static final int SECOND_MILLIS = 1000; // one second
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS; // one minute
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS; // one hour
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS; // one day



    public static String getTimeAgo(long time) {

        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *=1000L;
        }


        long now = System.currentTimeMillis(); // now in milli seconds - if u want them in seconds divide on 1000


        if (time > now || time <= 0) {
            return null;
        }

        final long diff = now - time;

        // if the difference is less than one day then it is sent today ? nope
//        if (diff < DAY_MILLIS) {
//
//            return "TODAY";
//
//        }else if (diff < 2 * DAY_MILLIS) {
//
//            return "YESTERDAY";
//
//        }else{

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy");
            Date netDate = (new Date(time));
            String date = sdf.format(netDate);

            return date;
//        }

//        if (diff < MINUTE_MILLIS) {
//            return "just now";
//        } else if (diff < 2 * MINUTE_MILLIS) {
//            return "a minute ago";
//        } else if (diff < 50 * MINUTE_MILLIS) {
//            return diff / MINUTE_MILLIS + " minutes ago";
//        } else if (diff < 90 * MINUTE_MILLIS) {
//            return "an hour ago";
//        } else if (diff < 24 * HOUR_MILLIS) {
//            return diff / HOUR_MILLIS + " hours ago";
//        } else if (diff < 48 * HOUR_MILLIS) {
//            return "yesterday";
//        } else {
//            return diff / DAY_MILLIS + " days ago";
//        }
    }

}
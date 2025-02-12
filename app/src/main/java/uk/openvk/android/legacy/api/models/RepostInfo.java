package uk.openvk.android.legacy.api.models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import uk.openvk.android.legacy.R;

public class RepostInfo implements Parcelable {
    public String name;
    public String time;
    public WallPost newsfeed_item;
    public RepostInfo(String original_author, long dt_sec, Context ctx) {
        name = original_author;
        Date dt = new Date(TimeUnit.SECONDS.toMillis(dt_sec));
        Date dt_midnight = new Date(System.currentTimeMillis() + 86400000);
        dt_midnight.setHours(0);
        dt_midnight.setMinutes(0);
        dt_midnight.setSeconds(0);
        if((dt_midnight.getTime() - (TimeUnit.SECONDS.toMillis(dt_sec))) < 86400000) {
            time = String.format("%s %s", ctx.getResources().getString(R.string.today_at), new SimpleDateFormat("HH:mm").format(dt));
        } else if((dt_midnight.getTime() - (TimeUnit.SECONDS.toMillis(dt_sec))) < (86400000 * 2)) {
            time = String.format("%s %s", ctx.getResources().getString(R.string.yesterday_at), new SimpleDateFormat("HH:mm").format(dt));
        } else if((dt_midnight.getTime() - (TimeUnit.SECONDS.toMillis(dt_sec))) < 31536000000L) {
            time = String.format("%s %s %s", new SimpleDateFormat("d MMMM").format(dt), ctx.getResources().getString(R.string.date_at), new SimpleDateFormat("HH:mm").format(dt));
        } else {
            time = String.format("%s %s %s", new SimpleDateFormat("d MMMM yyyy").format(dt), ctx.getResources().getString(R.string.date_at), new SimpleDateFormat("HH:mm").format(dt));
        }
    }

    protected RepostInfo(Parcel in) {
        name = in.readString();
        time = in.readString();
        newsfeed_item = in.readParcelable(WallPost.class.getClassLoader());
    }

    public static final Creator<RepostInfo> CREATOR = new Creator<RepostInfo>() {
        @Override
        public RepostInfo createFromParcel(Parcel in) {
            return new RepostInfo(in);
        }

        @Override
        public RepostInfo[] newArray(int size) {
            return new RepostInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(time);
        parcel.writeParcelable(newsfeed_item, i);
    }
}

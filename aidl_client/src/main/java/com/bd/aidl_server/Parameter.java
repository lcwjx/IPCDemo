package com.bd.aidl_server;

import android.os.Parcel;
import android.os.Parcelable;

public class Parameter implements Parcelable {
    private int param;

    public Parameter(int param) {
        this.param = param;
    }

    protected Parameter(Parcel in) {
        param = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(param);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Parameter> CREATOR = new Creator<Parameter>() {
        @Override
        public Parameter createFromParcel(Parcel in) {
            return new Parameter(in);
        }

        @Override
        public Parameter[] newArray(int size) {
            return new Parameter[size];
        }
    };

    public int getParam() {
        return param;
    }

    public void setParam(int param) {
        this.param = param;
    }
}

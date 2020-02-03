package com.clover.sdk.v1.printer.job;

/**
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.clover.sdk.v1.printer.Category;
import com.clover.sdk.v3.order.Order;

import java.util.ArrayList;


public class StaticLabelPrintJob extends StaticOrderBasedPrintJob implements Parcelable {

    public  ArrayList<String> itemIds;
    public  boolean reprintAllowed;
    public  boolean markPrinted;

    private static final String BUNDLE_KEY_ITEM_IDS = "i";
    private static final String BUNDLE_REPRINT_ALLOWED = "b";
    private static final String BUNDLE_KEY_MARK_PRINTED = "m";

    public static class Builder extends StaticOrderBasedPrintJob.Builder {

        protected ArrayList<String> itemIds;
        private boolean reprintAllowed = false;
        private boolean markPrinted = false;

        public Builder StaticLabelPrintJob(StaticLabelPrintJob pj) {
            staticOrderBasedPrintJob(pj);
            this.itemIds = pj.itemIds;
            this.reprintAllowed = pj.reprintAllowed;
            this.markPrinted = pj.markPrinted;
            return this;
        }

        public StaticLabelPrintJob.Builder itemIds(ArrayList<String> itemIds) {
            this.itemIds = itemIds;
            return this;
        }

        public StaticLabelPrintJob.Builder reprintAllowed(boolean allowed) {
            this.reprintAllowed = allowed;
            return this;
        }

        public StaticLabelPrintJob.Builder markPrinted(boolean markPrinted) {
            this.markPrinted = markPrinted;
            return this;
        }

        public StaticLabelPrintJob build() {
            return new StaticLabelPrintJob(this);
        }
    }


    protected StaticLabelPrintJob(Builder builder) {
        super(builder);
        this.itemIds = builder.itemIds;
        this.reprintAllowed = builder.reprintAllowed;
        this.markPrinted = builder.markPrinted;
    }

    public static final Creator<StaticLabelPrintJob> CREATOR = new Creator<StaticLabelPrintJob>() {
        public StaticLabelPrintJob createFromParcel(Parcel in) {
            return new StaticLabelPrintJob(in);
        }

        public StaticLabelPrintJob[] newArray(int size) {
            return new StaticLabelPrintJob[size];
        }
    };

    protected StaticLabelPrintJob(Parcel in) {
        super(in);
        Bundle bundle = in.readBundle(((Object)this).getClass().getClassLoader()); // needed otherwise BadParcelableException: ClassNotFoundException when unmarshalling
        itemIds = bundle.getStringArrayList(BUNDLE_KEY_ITEM_IDS);
        reprintAllowed = bundle.getBoolean(BUNDLE_REPRINT_ALLOWED);
        markPrinted = bundle.getBoolean(BUNDLE_KEY_MARK_PRINTED);
    }

    @Override
    public Category getPrinterCategory() {
        return Category.LABEL;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(BUNDLE_KEY_ITEM_IDS, itemIds);
        bundle.putBoolean(BUNDLE_REPRINT_ALLOWED, reprintAllowed);
        bundle.putBoolean(BUNDLE_KEY_MARK_PRINTED, markPrinted);
        dest.writeBundle(bundle);
    }
}


/*
 * Copyright 2016 Eric Liu
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

package com.olsplus.balancemall.component.ipicker.internal;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;


import com.olsplus.balancemall.component.ipicker.entities.Photo;

import java.util.ArrayList;
import java.util.List;


/**
 * A helper for querying all images from sd card.
 * <p>
 * Created by Eric on 16/9/12.
 */
public class ImageMedia {

    /**
     * Return a collection of image path.
     *
     * @param context
     * @return
     */
    public static List<Photo> queryAll(Context context) {
        List<Photo> photos = new ArrayList<>();

        // which image properties are we querying
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN,
                MediaStore.Images.Media.DATA,
        };

        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cur = MediaStore.Images.Media.query(
                context.getContentResolver(),
                images,                                             // Uri
                projection,                                         // Which columns to return
                null,                                               // Which rows to return (all rows)
                null,                                               // Selection arguments (none)
                MediaStore.Images.Media.DATE_TAKEN + " DESC"        // Ordering
        );

        if (cur.moveToFirst()) {
            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);
            do {
                // Get the field values
                photos.add(new Photo(cur.getString(dataColumn)));
            } while (cur.moveToNext());
        }

        return photos;
    }

    /**
     * Return the absolutely path of a uri.
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getFilePath(Context context, Uri uri) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

}

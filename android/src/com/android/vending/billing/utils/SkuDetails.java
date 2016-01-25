/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.vending.billing.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app product's listing details.
 */
public class SkuDetails
{
    private final String mItemType;
    private final String mSku;
    private final String mType;
    private final String mPrice;
    private final long   mPriceAmountMicros;
    private final String mPriceCurrencyCode;
    private final String mTitle;
    private final String mDescription;
    private final String mJson;

    public SkuDetails(String jsonSkuDetails) throws JSONException
    {
        this(IabHelper.ITEM_TYPE_INAPP, jsonSkuDetails);
    }

    public SkuDetails(String itemType, String jsonSkuDetails) throws JSONException
    {
        this.mItemType = itemType;
        this.mJson = jsonSkuDetails;
        JSONObject o = new JSONObject(this.mJson);
        this.mSku = o.optString("productId");
        this.mType = o.optString("type");
        this.mPrice = o.optString("price");
        this.mPriceAmountMicros = o.optLong("price_amount_micros");
        this.mPriceCurrencyCode = o.optString("price_currency_code");
        this.mTitle = o.optString("title");
        this.mDescription = o.optString("description");
    }

    public String getSku()
    {
        return this.mSku;
    }

    public String getType()
    {
        return this.mType;
    }

    public String getPrice()
    {
        return this.mPrice;
    }

    public long getPriceAmountMicros()
    {
        return this.mPriceAmountMicros;
    }

    public String getPriceCurrencyCode()
    {
        return this.mPriceCurrencyCode;
    }

    public String getTitle()
    {
        return this.mTitle;
    }

    public String getDescription()
    {
        return this.mDescription;
    }

    @Override
    public String toString()
    {
        return "SkuDetails:" + this.mJson;
    }
}

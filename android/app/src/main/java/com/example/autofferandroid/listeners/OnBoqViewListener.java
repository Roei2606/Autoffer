// OnBoqViewListener.java
package com.example.autofferandroid.listeners;

import com.example.chat_sdk.models.Message;

@FunctionalInterface
public interface OnBoqViewListener {
    void onBoqViewPdf(Message message);
}

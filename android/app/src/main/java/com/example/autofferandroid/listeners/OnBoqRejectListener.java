// OnBoqRejectListener.java
package com.example.autofferandroid.listeners;

import com.example.chat_sdk.models.Message;

@FunctionalInterface
public interface OnBoqRejectListener {
    void onBoqRejected(Message message);
}

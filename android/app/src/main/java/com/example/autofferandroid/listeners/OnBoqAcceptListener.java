// OnBoqAcceptListener.java
package com.example.autofferandroid.listeners;

import com.example.chat_sdk.models.Message;

@FunctionalInterface
public interface OnBoqAcceptListener {
    void onBoqAccepted(Message message);
}

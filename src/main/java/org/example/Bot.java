package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot
{
    public static void main( String[] args ) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMsg(Message message, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try{
            setButtons(sendMessage);
            execute(sendMessage);

        }catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public String getBotUsername() {
        return "TestBot";
    }

    public String getBotToken() {
        String botToken = System.getenv("TELEGRAM_BOT_TOKEN");
        return botToken;
    }

    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            switch(message.getText()){
                case "/help":
                    sendMsg(message, "Чем могу помочь?");
                    break;
                case"/setting":
                    sendMsg(message, "Что будем настраивать?");
                    break;
                default:
            }
        }
    }
    public void setButtons(SendMessage sendMessage){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/setting"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }
}

package weatherbot.domain;

import java.util.ArrayList;
import java.util.List;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

/**
 * Provides methods for creating different kinds of keyboards for reply messages.
 */
public class KeyboardBuilder {

    /**
     * Returns a keyboard for the main menu of the chat bot. 
     * @return returns replyKeyboardMarkup with main menu keyboard
     */
    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("help"));
        keyboardFirstRow.add(new KeyboardButton("units"));
        keyboardSecondRow.add(new KeyboardButton("my locations"));
        keyboardSecondRow.add(new KeyboardButton("search by city name"));
        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }

    /**
     * Creates and returns a keyboard for the unit settings request in the chat bot. 
     * Inline keyboard appears in the chat field as a reply to users request.
     * @return returns inlineKeyboardMarkup with units menu keyboard
     */
    public InlineKeyboardMarkup getUnitsKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new InlineKeyboardButton().setText("°C").setCallbackData("update_celcius"));
        rowInline.add(new InlineKeyboardButton().setText("°F").setCallbackData("update_fahrenheit"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);

        return markupInline;
    }

    /**
     * Returns a keyboard for the location settings request in the chat bot. 
     * @param reply a list of locations saved by user, if user has not saved any locations yet, 
     * the reply will contain an info message  
     * @return returns replyKeyboardMarkup with possible saved locations and options for adding new location or returning back
     */
    public ReplyKeyboardMarkup getLocationsKeyboard(String reply) {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyKeyboardMarkup();
        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        if (!reply.startsWith("No")) {
            String[] locations = reply.split(",");
            for (int i = 0; i < locations.length; i++) {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(locations[i]));
                keyboardRowList.add(row);
            }
        }
        KeyboardRow location = new KeyboardRow();
        location.add(new KeyboardButton("add a new location"));
        KeyboardRow back = new KeyboardRow();
        back.add(new KeyboardButton("back"));
        keyboardRowList.add(location);
        keyboardRowList.add(back);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        return replyKeyboardMarkup;
    }

    /**
     * Creates and returns replyKeyboardMarkup
     * @return replyKeyboardMarkup
     */
    public ReplyKeyboardMarkup createReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }

}

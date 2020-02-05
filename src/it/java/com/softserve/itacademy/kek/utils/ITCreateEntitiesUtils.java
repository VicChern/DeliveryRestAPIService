package com.softserve.itacademy.kek.utils;

import com.softserve.itacademy.kek.models.User;
import com.softserve.itacademy.kek.models.UserDetails;

import java.util.Random;
import java.util.UUID;

/**
 * Util class for creating entities in integration tests
 */
public class ITCreateEntitiesUtils {

    public static final int MAX_LENGTH_256 = 256;
    public static final int MAX_LENGTH_512 = 512;
    public static final int MAX_LENGTH_4096 = 4096;


    //================================================== User entity ==================================================

    /**
     * Gets {@link User} with simple fields (guid, name, nickname, email, phoneNumber) with fields depending on {@param i}
     *
     * @param i
     * @return user
     */
    public static User getOrdinaryUser(int i) {
        return getSimpleUser(
                UUID.randomUUID(),
                "name" + i,
                "nickname" + i,
                "email" + i + "@gmail.com",
                "380-50-444-55-55" + 1);
    }

    /**
     * Gets {@link User} with simple fields (guid, name, nickname, email, phoneNumber)
     *
     * @param guid
     * @param name
     * @param nickName
     * @param email
     * @param phoneNumber
     * @return user
     */
    public static User getSimpleUser(UUID guid, String name, String nickName, String email, String phoneNumber) {
        User user = new User();
        user.setName(name);
        user.setGuid(guid);
        user.setNickname(nickName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        return user;
    }

    /**
     * Gets {@link UserDetails} without {@link User}
     *
     * @return userDetails
     */
    public static UserDetails getSimpleUserDetailsWithValidFields() {
        UserDetails userDetails = new UserDetails();
        userDetails.setPayload(getRandomLetterString(500));
        userDetails.setImageUrl(getRandomLetterString(60));
        return userDetails;
    }

    public static UserDetails getSimpleUserDetailsWithNotValidPayload() {
        UserDetails userDetails = new UserDetails();
        userDetails.setPayload(getRandomLetterString(MAX_LENGTH_4096 + 1 + new Random().nextInt(50)));
        userDetails.setImageUrl(getRandomLetterString(60));
        return userDetails;
    }

    //================================================== common methods ==================================================

    /**
     * Gets random {@link String} with chars from 'a' to 'z' and from 'A' to 'Z' according to ASCII table
     *
     * @param stringLength length of random string
     * @return random string
     * @see <a href="http://www.asciitable.com">http://www.asciitable.com</a>
     */
    public static String getRandomLetterString(int stringLength) {
        char[] stringLetterSymbols;
        StringBuilder sb = new StringBuilder();
        for (char character = 'a'; character <= 'z'; character++) {
            sb.append(character);
        }
        for (char character = 'A'; character <= 'Z'; character++) {
            sb.append(character);
        }
        stringLetterSymbols = sb.toString().toCharArray();

        Random random = new Random();
        char[] buffer = new char[stringLength];
        for (int index = 0; index < stringLength; index++) {
            buffer[index] = stringLetterSymbols[random.nextInt(stringLetterSymbols.length)];
        }
        return new String(buffer);
    }

    /**
     * Gets random {@link String} with chars from '0' to '9' according to ASCII table
     *
     * @param stringLength length of random string
     * @return random string
     * @see <a href="http://www.asciitable.com">http://www.asciitable.com</a>
     */
    public static String getRandomNumberString(int stringLength) {
        char[] stringNumberSymbols;
        StringBuilder sb = new StringBuilder();
        for (char character = '0'; character <= '9'; character++) {
            sb.append(character);
        }
        stringNumberSymbols = sb.toString().toCharArray();

        Random random = new Random();
        char[] buffer = new char[stringLength];
        for (int index = 0; index < stringLength; index++) {
            buffer[index] = stringNumberSymbols[random.nextInt(stringNumberSymbols.length)];
        }
        return new String(buffer);
    }
}

package com.softserve.itacademy.kek.utils;

import com.softserve.itacademy.kek.models.Identity;
import com.softserve.itacademy.kek.models.IdentityType;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.models.TenantDetails;
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
     * Creates {@link User} with simple fields (guid, name, nickname, email, phoneNumber) with fields depending on {@param i}
     *
     * @param i
     * @return user
     */
    public static User createOrdinaryUser(int i) {
        return createSimpleUser(
                UUID.randomUUID(),
                "name" + i,
                "nickname" + i,
                "email" + i + "@gmail.com",
                "380-50-444-55-55" + i);
    }

    /**
     * Creates {@link User} with simple fields (guid, name, nickname, email, phoneNumber) and empty {@link UserDetails}
     *
     * @param guid
     * @param name
     * @param nickName
     * @param email
     * @param phoneNumber
     * @return user
     */
    public static User createSimpleUser(UUID guid, String name, String nickName, String email, String phoneNumber) {
        User user = new User();
        user.setName(name);
        user.setGuid(guid);
        user.setNickname(nickName);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        UserDetails userDetails = new UserDetails();
        user.setUserDetails(userDetails);
        userDetails.setUser(user);
        return user;
    }

    /**
     * Creates {@link UserDetails} with valid fields, but without {@link User}
     *
     * @return userDetails
     */
    public static UserDetails createSimpleUserDetailsWithValidFields() {
        UserDetails userDetails = new UserDetails();
        userDetails.setPayload(createRandomLetterString(500));
        userDetails.setImageUrl(createRandomLetterString(60));
        return userDetails;
    }

    /**
     * Creates {@link Identity} with valid fields
     *
     * @return identity
     */
    public static Identity createIdentity() {
        IdentityType identityType = createIdentityType();
        Identity identity = new Identity();
        identity.setPayload(createRandomLetterString(500));
        identity.setIdentityType(identityType);
        return identity;
    }

    /**
     * Creates {@link IdentityType} with valid fields
     *
     * @return identityType
     */
    public static IdentityType createIdentityType() {
        IdentityType identityType = new IdentityType();
        identityType.setName(createRandomLetterString(60));
        return identityType;
    }

    public static Tenant createTenantWithTenantDetails(int i) {
        return createSimpleTenant(
                UUID.randomUUID(),
                "name" + i
        );
    }

    public static Tenant createSimpleTenant(UUID guid, String name) {
        Tenant tenant = new Tenant();
        tenant.setName(name);
        tenant.setGuid(guid);

        TenantDetails tenantDetails = new TenantDetails();
        tenantDetails.setPayload(createRandomLetterString(500));
        tenantDetails.setImageUrl(createRandomLetterString(60));

        tenant.setTenantDetails(tenantDetails);
        tenantDetails.setTenant(tenant);

        return tenant;
    }
    //================================================== common methods ==================================================

    /**
     * Creates random {@link String} with chars from 'a' to 'z' and from 'A' to 'Z' according to ASCII table
     *
     * @param stringLength length of random string
     * @return random string
     * @see <a href="http://www.asciitable.com">http://www.asciitable.com</a>
     */
    public static String createRandomLetterString(int stringLength) {
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
     * Creates random {@link String} with chars from '0' to '9' according to ASCII table
     *
     * @param stringLength length of random string
     * @return random string
     * @see <a href="http://www.asciitable.com">http://www.asciitable.com</a>
     */
    public static String createRandomNumberString(int stringLength) {
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

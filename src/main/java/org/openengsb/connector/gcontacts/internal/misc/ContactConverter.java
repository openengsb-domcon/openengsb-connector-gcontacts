/**
 * Licensed to the Austrian Association for Software Tool Integration (AASTI)
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. The AASTI licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openengsb.connector.gcontacts.internal.misc;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.openengsb.core.common.util.ModelUtils;
import org.openengsb.domain.contact.models.Contact;
import org.openengsb.domain.contact.models.InformationTypeWithValue;
import org.openengsb.domain.contact.models.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.contacts.Birthday;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.Event;
import com.google.gdata.data.contacts.Website;
import com.google.gdata.data.extensions.City;
import com.google.gdata.data.extensions.Country;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.FullName;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.data.extensions.PostCode;
import com.google.gdata.data.extensions.Region;
import com.google.gdata.data.extensions.Street;
import com.google.gdata.data.extensions.StructuredPostalAddress;
import com.google.gdata.data.extensions.When;

/**
 * does the converting work between contact model from contact domain and contact model of the google framework
 */
public final class ContactConverter {

    private static Logger LOGGER = LoggerFactory.getLogger(ContactConverter.class);
    
    private ContactConverter() {
    }

    /**
     * converts a contact object of the contact domain to a contact entry for google
     */
    public static ContactEntry convertContactToContactEntry(Contact contact) {
        return extendContactEntryWithContact(new ContactEntry(), contact);
    }

    /**
     * extends a contact entry with informations of a contact object of the contact domain
     */
    public static ContactEntry extendContactEntryWithContact(ContactEntry entry, Contact contact) {
        entry.setContent(new PlainTextConstruct(contact.getComment()));

        Name name = new Name();
        name.setFullName(new FullName(contact.getName(), null));
        entry.setName(name);

        entry.getEmailAddresses().clear();
        if (contact.getMails() != null) {
            for (InformationTypeWithValue<String> mail : contact.getMails()) {
                Email eMail = new Email();
                eMail.setLabel(mail.getKey());
                eMail.setAddress(mail.getValue());
                entry.addEmailAddress(eMail);
            }
        }

        entry.getPhoneNumbers().clear();
        if (contact.getTelephones() != null) {
            for (InformationTypeWithValue<String> phone : contact.getTelephones()) {
                PhoneNumber number = new PhoneNumber();
                number.setLabel(phone.getKey());
                number.setPhoneNumber(phone.getValue());
                entry.addPhoneNumber(number);
            }
        }

        entry.getWebsites().clear();
        if (contact.getHomepages() != null) {
            for (InformationTypeWithValue<String> site : contact.getHomepages()) {
                Website web = new Website();
                web.setLabel(site.getKey());
                web.setHref(site.getValue());
                entry.addWebsite(web);
            }
        }

        entry.getEvents().clear();
        entry.setBirthday(null);
        if (contact.getDates() != null) {
            for (InformationTypeWithValue<Date> date : contact.getDates()) {
                if (date.getKey().matches(".*birthday.*")) {
                    Birthday birth = new Birthday();
                    Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                    birth.setWhen(formatter.format(date.getValue()));
                    entry.setBirthday(birth);
                } else {
                    Event ev = new Event();
                    DateTime startTime = new DateTime(date.getValue(), TimeZone.getDefault());
                    startTime.setDateOnly(true);
                    DateTime endTime = new DateTime(date.getValue(), TimeZone.getDefault());
                    endTime.setDateOnly(true);
                    When w = new When();
                    w.setStartTime(startTime);
                    w.setEndTime(endTime);
                    ev.setWhen(w);
                    ev.setLabel(date.getKey());
                    entry.addEvent(ev);
                }
            }
        }

        entry.getStructuredPostalAddresses().clear();
        if (contact.getLocations() != null) {
            for (InformationTypeWithValue<Location> location : contact.getLocations()) {
                StructuredPostalAddress address = new StructuredPostalAddress();
                address.setLabel(location.getKey());
                Location loc = location.getValue();

                String temp = loc.getCountry();
                if (temp != null) {
                    Country country = new Country();
                    country.setValue(temp);
                    address.setCountry(country);
                }
                temp = loc.getState();
                if (temp != null) {
                    Region region = new Region();
                    region.setValue(temp);
                    address.setRegion(region);
                }
                temp = loc.getCity();
                if (temp != null) {
                    City city = new City();
                    city.setValue(temp);
                    address.setCity(city);
                }
                temp = loc.getZip();
                if (temp != null) {
                    PostCode postcode = new PostCode();
                    postcode.setValue(temp);
                    address.setPostcode(postcode);
                }
                temp = loc.getAddress();
                if (temp != null) {
                    Street street = new Street();
                    street.setValue(temp);
                    address.setStreet(street);
                }
                entry.addStructuredPostalAddress(address);
            }
        }
        return entry;
    }

    /**
     * converts a contact entry of google api to a contact object of contact domain
     */
    public static Contact convertContactEntryToContact(ContactEntry entry) {
        Contact contact = ModelUtils.createEmptyModelObject(Contact.class);

        contact.setId(entry.getId());
        contact.setComment(entry.getPlainTextContent());

        ArrayList<InformationTypeWithValue<String>> mails = new ArrayList<InformationTypeWithValue<String>>();

        for (Email mail : entry.getEmailAddresses()) {
            @SuppressWarnings("unchecked")
            InformationTypeWithValue<String> itwv = ModelUtils.createEmptyModelObject(InformationTypeWithValue.class);
            itwv.setKey(mail.getLabel());
            itwv.setValue(mail.getAddress());
            mails.add(itwv);
        }

        contact.setMails(mails);

        ArrayList<InformationTypeWithValue<String>> numbers = new ArrayList<InformationTypeWithValue<String>>();

        for (PhoneNumber number : entry.getPhoneNumbers()) {
            @SuppressWarnings("unchecked")
            InformationTypeWithValue<String> itwv = ModelUtils.createEmptyModelObject(InformationTypeWithValue.class);
            itwv.setKey(number.getLabel());
            itwv.setValue(number.getPhoneNumber());
            numbers.add(itwv);
        }

        contact.setTelephones(numbers);

        ArrayList<InformationTypeWithValue<String>> sites = new ArrayList<InformationTypeWithValue<String>>();

        for (Website site : entry.getWebsites()) {
            @SuppressWarnings("unchecked")
            InformationTypeWithValue<String> itwv = ModelUtils.createEmptyModelObject(InformationTypeWithValue.class);
            itwv.setKey(site.getLabel());
            itwv.setValue(site.getHref());
            sites.add(itwv);
        }

        contact.setHomepages(sites);

        ArrayList<InformationTypeWithValue<Date>> dates = new ArrayList<InformationTypeWithValue<Date>>();

        for (Event event : entry.getEvents()) {
            String key = event.getLabel();
            Date date = new Date(event.getWhen().getStartTime().getValue());
            @SuppressWarnings("unchecked")
            InformationTypeWithValue<Date> itwv = ModelUtils.createEmptyModelObject(InformationTypeWithValue.class);
            itwv.setKey(key);
            itwv.setValue(date);
            dates.add(itwv);
        }

        Birthday birthday = entry.getBirthday();
        if (birthday != null) {
            Date date;
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date = formatter.parse(birthday.getWhen());
                @SuppressWarnings("unchecked")
                InformationTypeWithValue<Date> itwv = ModelUtils.createEmptyModelObject(InformationTypeWithValue.class);
                itwv.setKey("birthday");
                itwv.setValue(date);
                dates.add(itwv);
            } catch (ParseException e) {
                LOGGER.error("unable to parse date from google", e);
            }
        }
        contact.setDates(dates);

        ArrayList<InformationTypeWithValue<Location>> locations = new ArrayList<InformationTypeWithValue<Location>>();

        for (StructuredPostalAddress address : entry.getStructuredPostalAddresses()) {
            Location loc = ModelUtils.createEmptyModelObject(Location.class);
            loc.setCountry(address.getCountry().getValue());
            loc.setState(address.getRegion().getValue());
            loc.setCity(address.getCity().getValue());
            loc.setZip(address.getPostcode().getValue());
            loc.setAddress(address.getStreet().getValue());
            @SuppressWarnings("unchecked")
            InformationTypeWithValue<Location> itwv = ModelUtils.createEmptyModelObject(InformationTypeWithValue.class);
            itwv.setKey(address.getLabel());
            itwv.setValue(loc);
            locations.add(itwv);
        }

        contact.setLocations(locations);

        contact.setName(entry.getName().getFullName().getValue());

        return contact;
    }
}

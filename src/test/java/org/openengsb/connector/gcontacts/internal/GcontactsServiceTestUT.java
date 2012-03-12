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

package org.openengsb.connector.gcontacts.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openengsb.core.api.DomainMethodExecutionException;
import org.openengsb.core.api.ekb.PersistInterface;
import org.openengsb.core.common.util.ModelUtils;
import org.openengsb.domain.contact.Contact;
import org.openengsb.domain.contact.InformationTypeWithValue;
import org.openengsb.domain.contact.Location;

public class GcontactsServiceTestUT {
    private static GcontactsServiceImpl service;
    private static final String USERNAME = "openengsb.notification.test@gmail.com";
    private static final String PASSWORD = "pwd-openengsb";

    private static ArrayList<Contact> cons;

    @BeforeClass
    public static void initiate() {
        cons = new ArrayList<Contact>();
        
        service = new GcontactsServiceImpl("id");
        service.setGoogleUser(USERNAME);
        service.setGooglePassword(PASSWORD);
        
        PersistInterface persistInterface = mock(PersistInterface.class);
        service.setPersistInterface(persistInterface);        
    }

    private Contact createTestContact(String name) {
        Contact contact = ModelUtils.createEmptyModelObject(Contact.class);

        contact.setName(name);

        ArrayList<InformationTypeWithValue<String>> mails = new ArrayList<InformationTypeWithValue<String>>();
        mails.add(new TestInformationTypeWithValue<String>("privat", "test@testacoount.com"));
        contact.setMails(mails);

        ArrayList<InformationTypeWithValue<String>> phones = new ArrayList<InformationTypeWithValue<String>>();
        phones.add(new TestInformationTypeWithValue<String>("büro", "023803409328409234"));
        contact.setTelephones(phones);

        ArrayList<InformationTypeWithValue<String>> sites = new ArrayList<InformationTypeWithValue<String>>();
        sites.add(new TestInformationTypeWithValue<String>("homepage", "openengsb.org"));
        contact.setHomepages(sites);

        ArrayList<InformationTypeWithValue<Date>> dates = new ArrayList<InformationTypeWithValue<Date>>();
        dates.add(new TestInformationTypeWithValue<Date>("birthday", new Date()));
        dates.add(new TestInformationTypeWithValue<Date>("jahrestag", new Date()));
        contact.setDates(dates);

        ArrayList<InformationTypeWithValue<Location>> locations = new ArrayList<InformationTypeWithValue<Location>>();
        Location location = ModelUtils.createEmptyModelObject(Location.class);
        location.setCountry("Austria");
        location.setState("Vienna");
        location.setCity("Vienna");
        location.setZip("1040");
        location.setAddress("Taubstummengasse 11");
        locations.add(new TestInformationTypeWithValue<Location>("Headquarters", location));

        contact.setLocations(locations);

        contact.setComment("this is a testcomment");

        return contact;
    }

    @Test
    public void testCreateContact() {
        Contact contact = createTestContact("testcontact-Create");
        service.createContact(contact);

        cons.add(contact);

        assertNotNull(contact.getId());
    }

    @Test
    public void testUpdateContact() {
        Contact contact = createTestContact("testcontact-Update");
        service.createContact(contact);

        contact.setName("testcontact new");
        service.updateContact(contact);

        String id = contact.getId();
        contact = null;

        contact = service.loadContact(id);

        cons.add(contact);

        assertEquals(contact.getName(), "testcontact new");
    }

    @Test
    public void testDeleteContact() {
        Contact contact = createTestContact("testcontact-Delete");
        service.createContact(contact);

        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            // must be done because if the interval between create and delete
            // is too short, the delete often don't work
        }

        String id = contact.getId();

        service.deleteContact(id);

        boolean works = false;

        try {
            contact = service.loadContact(id);
        } catch (DomainMethodExecutionException e) {
            works = true;
        }

        assertTrue(works);
    }

    @Test
    public void testRetrieveContactsByName() {
        Contact contact = createTestContact("testcontact-Retrieve");
        service.createContact(contact);

        ArrayList<Contact> contacts = service.retrieveContacts(null, "testcontact-Retrieve", null, null, null, null);

        cons.add(contact);

        assertTrue(contacts.size() > 0);
    }

    @Test
    public void testRetrieveContactsByLocation() {
        Contact contact = createTestContact("testcontact-Retrieve");
        service.createContact(contact);

        Location location = ModelUtils.createEmptyModelObject(Location.class);
        location.setAddress("Taubstummengasse 11");

        ArrayList<Contact> contacts = service.retrieveContacts(null, null, null, location, null, null);

        cons.add(contact);

        assertTrue(contacts.size() > 0);
    }

    @AfterClass
    public static void cleanUp() {
        for (Contact contact : cons) {
            service.deleteContact(contact.getId());
        }
    }

}

package edu.osu.cse.todolist.to_dolist;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Unit test cases for Model classes
 */
public class ModelUnitTest {

    /**
     * Test newly created empty Task
     *
     * @throws Exception
     */
    @Test
    public void testEmptyTask() throws Exception {
        Task task = new Task();
        // Id
        assertEquals("Task Id is not -1", -1, task.getId());
        // Title
        assertTrue("Task Title is not empty", task.getTitle().isEmpty());
        // Note
        assertTrue("Task Note is not empty", task.getNote().isEmpty());
        // Config
        assertTrue("Task default Config is not NONE",
                Task.ConfigType.NONE.equals(task.getConfig()));
        // Schedule
        assertNull("Task Schedule is not null", task.getSchedule());
        assertNull("Task Schedule Date is not null", task.getRemindDate());
        assertTrue("Task Schedule Date string is not null", task.getRemindDateString().isEmpty());
        // Location
        assertNull("Task Location is not null", task.getLocation());
        // Complete
        assertFalse("Task is complete", task.isComplete());
        // Starred
        assertFalse("Task is starred", task.isStarred());
        // Enable
        assertFalse("Task remind is enabled", task.isEnabled());
    }

    /**
     * The if the the setters and getters of Task work correctly
     *
     * @throws Exception
     */
    @Test
    public void testTaskSettersAndGetters() throws Exception {
        Task task = new Task();

        // Title
        String title = "Task Title";
        task.setTitle(title);
        assertEquals("Task Title setter/getter incorrectly",
                title, task.getTitle());

        // Note
        String Note = "Test Task Note";
        task.setNote(Note);
        assertEquals("Task Note setter/getter incorrectly",
                Note, task.getNote());

        // Config
        Task.ConfigType config = Task.ConfigType.TIME;
        task.setConfig(config);
        assertEquals("Task Config setter/getter incorrectly",
                config, task.getConfig());

        // set task complete true/false
        boolean complete = true;
        task.setComplete(complete);
        assertEquals("Task Complete setter/getter incorrectly", complete, task.isComplete());

        complete = false;
        task.setComplete(complete);
        assertEquals("Task Complete setter/getter incorrectly", complete, task.isComplete());

        // set task Starred true/false
        boolean starred = true;
        task.setStarred(starred);
        assertEquals("Task Starred setter/getter incorrectly", starred, task.isStarred());

        starred = false;
        task.setStarred(starred);
        assertEquals("Task Starred setter/getter incorrectly", starred, task.isStarred());

        // set task Starred true/false
        boolean enabled = true;
        task.setEnabled(enabled);
        assertEquals("Task Enabled setter/getter incorrectly", enabled, task.isEnabled());

        enabled = false;
        task.setEnabled(enabled);
        assertEquals("Task Enabled setter/getter incorrectly", enabled, task.isEnabled());
    }

    @Test
    public void testTaskRemindDate() throws Exception {
        Task task = new Task();
        Date date = new Date();
        task.setRemindDate(date);
        assertEquals("Task set/get RemindDate incorrectly", date, task.getRemindDate());

        // TODO: test getRemindDateString
    }

    /**
     * Test newly created empty Schedule
     *
     * @throws Exception
     */
    @Test
    public void testEmptySchedule() throws Exception {
        Task task = new Task();
        Schedule schedule = new Schedule(task);
        // Id
        assertEquals("Schedule Id is not -1", -1, schedule.getId());
        // Task
        assertEquals("Schedule associated task is not equal", task, schedule.getTask());
        // Config
        assertTrue("Schedule default Config is not NONE",
                Schedule.ConfigType.NONE.equals(schedule.getConfig()));
        // Date
        assertNull("Schedule Date is not null", schedule.getDate());
    }

    /**
     * Test setters and getters of Schedule Model
     *
     * @throws Exception
     */
    @Test
    public void testScheduleSettersAndGetters() throws Exception {
        Task task = new Task();
        Schedule schedule = new Schedule(task);

        Date date = new Date();
        schedule.setDate(date);
        assertEquals("Schedule Date setter/getter incorrectly", date, schedule.getDate());
    }

    /**
     * Test newly created empty Location
     *
     * @throws Exception
     */
    @Test
    public void testEmptyLocation() throws Exception {
        Location location = new Location();
        // Id
        assertEquals("Location Id is not -1", -1, location.getId());
        // Title
        assertTrue("Location Title is not empty", location.getTitle().isEmpty());
        // Note
        assertTrue("Location Note is not empty", location.getNote().isEmpty());
        // Config
        assertTrue("Location default Config is not GPS",
                Location.ConfigType.GPS.equals(location.getConfig()));
    }

    /**
     * Test setters and getters of Location Model
     *
     * @throws Exception
     */
    @Test
    public void testLocationSettersAndGetters() throws Exception {
        Location location = new Location();

        // Title
        String title = "Location Title";
        location.setTitle(title);
        assertEquals("Location Title setter/getter incorrectly",
                title, location.getTitle());

        // Note
        String Note = "Test Location Note";
        location.setNote(Note);
        assertEquals("Location Note setter/getter incorrectly",
                Note, location.getNote());

        // Config
        Location.ConfigType config = Location.ConfigType.WiFi;
        location.setConfig(config);
        assertEquals("Location Config setter/getter incorrectly",
                config, location.getConfig());

        config = Location.ConfigType.GPS;
        location.setConfig(config);
        assertEquals("Location Config setter/getter incorrectly",
                config, location.getConfig());

    }

    /**
     * Test newly created empty WiFiPosition
     *
     * @throws Exception
     */
    @Test
    public void testEmptyWiFiPosition() throws Exception {
        WiFiPosition wifiPosition = new WiFiPosition();
        // Id
        assertEquals("WiFiPosition Id is not -1", -1, wifiPosition.getId());
        // SSID
        assertTrue("WiFiPosition SSID is not empty", wifiPosition.getSSID().isEmpty());
        // BSSID
        assertTrue("WiFiPosition BSSID is not empty", wifiPosition.getBSSID().isEmpty());

        // MaxSignal
        assertEquals("WiFiPosition Max Signal is not 100", 100, wifiPosition.getMaxSignal(), 0);
        // MinSignal
        assertEquals("WiFiPosition Min Signal is not 11", 1, wifiPosition.getMinSignal(), 0);
    }

    /**
     * Test setters and getters of Location Model
     *
     * @throws Exception
     */
    @Test
    public void testWiFiPositionSettersAndGetters() throws Exception {
        WiFiPosition wiFiPosition = new WiFiPosition();

        // SSID
        String ssid = "osuwireless";
        wiFiPosition.setSSID(ssid);
        assertEquals("WiFiPosition SSID setter/getter incorrectly",
                ssid, wiFiPosition.getSSID());

        // BSSID
        String bssid = "A0:B0:C0:D0:E0:F0:00:00";
        wiFiPosition.setBSSID(bssid);
        assertEquals("WiFiPosition BSSID setter/getter incorrectly",
                bssid, wiFiPosition.getBSSID());

        // Max Signal
        double signal = 95;
        wiFiPosition.setMaxSignal(signal);
        assertEquals("WiFiPosition MaxSignal setter/getter incorrectly",
                signal, wiFiPosition.getMaxSignal(), 0);

        // Min Signal
        signal = 15;
        wiFiPosition.setMinSignal(signal);
        assertEquals("WiFiPosition MinSignal setter/getter incorrectly",
                signal, wiFiPosition.getMinSignal(), 0);

    }

    /**
     * Test newly created empty GPSCoordinate
     *
     * @throws Exception
     */
    @Test
    public void testEmptyGPSCoordinate() throws Exception {
        GPSCoordinate gps = new GPSCoordinate();
        // Id
        assertEquals("GPSCoordinate Id is not -1", -1, gps.getId());
        // Latitude
        assertEquals("GPSCoordinate Latitude is not 0", 0, gps.getLatitude(), 0);
        // Longitude
        assertEquals("GPSCoordinate Longitude is not 0", 0, gps.getLongitude(), 0);
        // Address
        assertTrue("GPSCoordinate Address is not empty", gps.getAddress().isEmpty());
        // PlaceId
        assertTrue("GPSCoordinate PlaceId is not empty", gps.getPlaceId().isEmpty());
    }

    /**
     * Test setters and getters of GPSCoordinate Model
     *
     * @throws Exception
     */
    @Test
    public void testGPSCoordinateSettersAndGetters() throws Exception {
        GPSCoordinate gps = new GPSCoordinate();

        // Latitude
        double latitude = 50.5050;
        gps.setLatitude(latitude);
        assertEquals("GPSCoordinate Latitude setter/getter incorrectly",
                latitude, gps.getLatitude(), 0);

        // Longitude
        double longitude = 60.6060;
        gps.setLongitude(longitude);
        assertEquals("GPSCoordinate Longitude setter/getter incorrectly",
                longitude, gps.getLongitude(), 0);

        // Range
        double range = 123.456;
        gps.setRange(range);
        assertEquals("GPSCoordinate Range setter/getter incorrectly",
                range, gps.getRange(), 0);

        // Address
        String address = "Google Mountain View 1600 Amphitheatre Parkway Mountain View, CA 94043";
        gps.setAddress(address);
        assertEquals("Location Address setter/getter incorrectly",
                address, gps.getAddress());

        // PlaceId
        String PlaceId = "XXXXXXX";
        gps.setPlaceId(PlaceId);
        assertEquals("Location PlaceId setter/getter incorrectly",
                PlaceId, gps.getPlaceId());
    }
}

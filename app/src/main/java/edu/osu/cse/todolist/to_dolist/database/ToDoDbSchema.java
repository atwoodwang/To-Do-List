package edu.osu.cse.todolist.to_dolist.database;

/**
 * To-Do List Database Schema declaration class
 */
public class ToDoDbSchema {
    /**
     * Task table schema
     */
    public static final class TaskTable {
        /**
         * Table Name: Task
         */
        public static final String NAME = "Task";

        public static final class Cols {
            /**
             * Primary key
             */
            public static final String ID = "ID";
            // index TITLE column if we provide search function in future
            public static final String TITLE = "Title";
            public static final String NOTE = "Note";
            public static final String STARRED = "Starred";
            public static final String CONFIG = "Config";
            public static final String COMPLETE = "Complete";
        }
    }

    /**
     * Schedule table schema
     */
    public static final class ScheduleTable {
        /**
         * Table Name: Schedule
         */
        public static final String NAME = "Schedule";

        public static final class Cols {
            /**
             * Primary key
             */
            public static final String ID = "ID";
            /**
             * Foreign key reference to Task
             */
            public static final String TASK_ID = "Task_ID";
            public static final String CONFIG = "Config";
            public static final String DATE = "Date";
        }
    }

    /**
     * Location table schema
     */
    public static final class LocationTale {
        /**
         * Table Name: Location
         */
        public static final String NAME = "Location";

        public static final class Cols {
            /**
             * Primary key
             */
            public static final String ID = "ID";
            public static final String TITLE = "Title";
            public static final String NOTE = "Note";
            public static final String CONFIG = "Config";
        }
    }

    /**
     * Join table of Task and Location
     */
    public static final class TaskLocationTable {
        /**
         * Table Name: Task_Location
         */
        public static final String NAME = "Task_Location";

        public static final class Cols {
            /**
             * Primary key
             */
            public static final String ID = "ID";
            public static final String TASK_ID = "Task_ID";
            public static final String LOCATION_ID = "Location_ID";
        }
    }

    /**
     * GPSCoordinate table schema
     */
    public static final class GPSCoordinateTable {
        /**
         * Table Name: GPSCoordinate
         */
        public static final String NAME = "GPSCoordinate";

        public static final class Cols {
            /**
             * Primary key
             */
            public static final String ID = "ID";
            /**
             * Foreign key reference to Location
             */
            public static final String LOCATION_ID = "Location_ID";
            public static final String LONGITUDE = "Longitude";
            public static final String LATITUDE = "Latitude";
            public static final String RANGE = "Range";
        }
    }

    /**
     * WiFiPosition table schema
     */
    public static final class WiFiPosition {
        /**
         * Table Name: WiFiPosition
         */
        public static final String NAME = "WiFiPosition";

        public static final class Cols {
            /**
             * Primary key
             */
            public static final String ID = "ID";
            /**
             * Foreign key reference to Location
             */
            public static final String LOCATION_ID = "Location_ID";
            public static final String SSID = "SSID";
            public static final String MAC = "MAC";
            public static final String MAXSIGNAL = "MaxSignal";
            public static final String MINSIGNAL = "MinSignal";

        }
    }
}

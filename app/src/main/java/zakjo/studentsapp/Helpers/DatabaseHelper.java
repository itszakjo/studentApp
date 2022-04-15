package zakjo.studentsapp.Helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.mbms.StreamingServiceInfo;

import zakjo.studentsapp.model.TalkingTo;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contactslist.db";

    public static final String CONTACTS_TABLE = "Contacts";
    public static final String NAME = "NAME";
    public static final String PHONE = "PHONE";

    public static final String TALKINGTO_TABLE = "talking_to";
    public static final String IMAGEURL = "IMAGEURL";
    public static final String LASTMESSAGE = "LASTMESSAGE";
    public static final String CHATNAME = "CHATNAME";
    public static final String CHATPHONE = "CHATPHONE";
    public static final String STATUS = "STATUS";
    public static final String TIMESTAMP = "TIMESTAMP";
    public static final String TYPINGTO = "TYPINGTO";
    public static final String CHATYPE = "CHATYPE";

    public static final String MESSAGES_TABLE = "messages";
    public static final String USER_PHONE = "USERPHONE";
    public static final String SENDER = "SENDER";
    public static final String MESSGAE = "MESSAGE";
    public static final String TYPE = "TYPE";
    public static final String ISEEN = "ISEEN";
    public static final String MTIMESTAMP = "MTIMESTAMP";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 5);
        // DB_VERSION is an int,update it every new build , it is 3 now , update it next time u edit the db
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + CONTACTS_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, PHONE TEXT,NAME TEXT)";
        String createChatsTable = "CREATE TABLE " + TALKINGTO_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, IMAGEURL TEXT,LASTMESSAGE TEXT , CHATNAME TEXT ,CHATPHONE TEXT , STATUS TEXT , TIMESTAMP TEXT , TYPINGTO TEXT , CHATYPE TEXT)";
        String createMessagesTable = "CREATE TABLE " + MESSAGES_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, USERPHONE TEXT,SENDER TEXT , MESSAGE TEXT ,TYPE TEXT , ISEEN INTEGER , MTIMESTAMP TEXT , CHATYPE TEXT)";

        db.execSQL(createChatsTable);
        db.execSQL(createTable);
        db.execSQL(createMessagesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS " + CONTACTS_TABLE);
        db.execSQL("DROP TABLE IF  EXISTS " + TALKINGTO_TABLE);
        db.execSQL("DROP TABLE IF  EXISTS " + MESSAGES_TABLE);
        onCreate(db);
    }

    public boolean addData( String Name ,String Phone ) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, Name);
        contentValues.put(PHONE, Phone);


        long result = db.insert(CONTACTS_TABLE, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addTalkingToData(String imageUrl , String lastMessage ,  String Name ,
                                    String Phone , String Status , String timeStamp, String typing_to ) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(IMAGEURL, imageUrl);
        contentValues.put(LASTMESSAGE, lastMessage);
        contentValues.put(CHATNAME, Name);
        contentValues.put(CHATPHONE, Phone);
        contentValues.put(STATUS, Status);
        contentValues.put(TIMESTAMP, timeStamp);
        contentValues.put(TYPINGTO, typing_to);


        long result = db.insert(TALKINGTO_TABLE, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + CONTACTS_TABLE, null);
        return data;
    }

    public Cursor getListTalkingTo(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TALKINGTO_TABLE + " ORDER BY TIMESTAMP", null);
        return data;
    }

    public Cursor getMessagesList(String userphone){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT SENDER,MESSAGE,TYPE,ISEEN,MTIMESTAMP FROM " + MESSAGES_TABLE + " WHERE USERPHONE='"+userphone+"' ORDER BY MTIMESTAMP", null);
        return data;
    }

    public Cursor getUnsentMessagesList(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT USERPHONE,MESSAGE,TYPE,MTIMESTAMP,CHATYPE FROM " + MESSAGES_TABLE + " WHERE ISEEN= 10 ORDER BY MTIMESTAMP", null);
        return data;
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = ("DELETE FROM "+CONTACTS_TABLE+" WHERE ID='"+id+"'");
        db.execSQL(query);
    }

    public void updateOrInsert(String name,String phone){
        SQLiteDatabase db = this.getWritableDatabase();

        name=name.replace("'","''");


        String query = ("insert or replace into "+CONTACTS_TABLE +"(ID,PHONE,NAME) values ((select ID from "+CONTACTS_TABLE+" where PHONE = '"+phone+"' ), '"+phone+"', '"+name+"');");
        db.execSQL(query);
    }

    public void updateOrInsertInTalkingTo(String imageUrl , String lastMessage ,  String Name ,
                                          String Phone , String Status , String timeStamp, String typing_to  , String chat_type){

        SQLiteDatabase db = this.getWritableDatabase();

        if(lastMessage == null || lastMessage.equals("")){ lastMessage = "" ; }
        if( Name == null  || Name.equals("") ){ Name = "" ; }

        lastMessage=lastMessage.replace("'","''");
        Name=Name.replace("'","''");


        String query = ("insert or replace into "+TALKINGTO_TABLE +"(ID,IMAGEURL,LASTMESSAGE , CHATNAME ,CHATPHONE ,STATUS , TIMESTAMP , TYPINGTO ,CHATYPE) values ((select ID from "+TALKINGTO_TABLE+" where CHATPHONE = '"+Phone+"' ), '"+imageUrl+"', '"+lastMessage+"' , '"+Name+"' , '"+Phone+"' , '"+Status+"' , '"+timeStamp+"' , '"+typing_to+"' , '"+chat_type+"' );");
        db.execSQL(query);
    }

    public void updateOrInsertInMessages(String userphone ,String sender ,String message,
                                          String type , int iseen , String timeStamp , String chatType ){

        SQLiteDatabase db = this.getWritableDatabase();
        if(message == null || message.equals("")){ message = "" ; }
        message=message.replace("'","''");
        String query = "insert or replace into "+ MESSAGES_TABLE + "(ID, USERPHONE, SENDER, MESSAGE, TYPE, ISEEN, MTIMESTAMP, CHATYPE ) values((select ID from "+MESSAGES_TABLE+" where MTIMESTAMP = '"+timeStamp+"' ) ,'"+userphone+"'  , '"+sender+"' , '"+message+"' , '"+type+"' , '"+iseen+"' , '"+timeStamp+"' , '"+chatType+"')";
        db.execSQL(query);
    }

}
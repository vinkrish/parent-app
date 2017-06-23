package com.aanglearning.parentapp.sqlite;

interface SqlConstant {

    String DATABASE_NAME = "parent.db";
    int DATABASE_VERSION = 1;

    String CREATE_ATTENDANCE = "CREATE TABLE attendance (" +
            "  Id INTEGER PRIMARY KEY," +
            "  SectionId INTEGER," +
            "  StudentId INTEGER," +
            "  StudentName TEXT," +
            "  SubjectId INTEGER," +
            "  Type TEXT," +
            "  Session INTEGER," +
            "  DateAttendance date," +
            "  TypeOfLeave TEXT" +
            ")";

    String CREATE_CLASS = "CREATE TABLE class (" +
            "  Id INTEGER," +
            "  ClassName TEXT," +
            "  SchoolId INTEGER," +
            "  TeacherId INTEGER," +
            "  AttendanceType TEXT" +
            ")";

    String CREATE_HOMEWORK = "CREATE TABLE homework (" +
            "  Id INTEGER," +
            "  SectionId INTEGER," +
            "  SubjectId INTEGER," +
            "  SubjectName TEXT," +
            "  HomeworkMessage TEXT," +
            "  HomeworkDate date" +
            ")";

    String CREATE_SCHOOL = "CREATE TABLE school (" +
            "  Id INTEGER," +
            "  SchoolName TEXT," +
            "  Website TEXT," +
            "  ShortenedSchoolName TEXT," +
            "  ContactPersonName TEXT," +
            "  AdminUsername TEXT," +
            "  AdminPassword TEXT," +
            "  Landline TEXT," +
            "  Mobile1 TEXT," +
            "  Mobile2 TEXT," +
            "  Email TEXT," +
            "  Street TEXT," +
            "  City TEXT," +
            "  District TEXT," +
            "  State TEXT," +
            "  Pincode TEXT," +
            "  PrincipalId INTEGER," +
            "  NumberOfStudents INTEGER" +
            ")";

    String CREATE_SECTION = "CREATE TABLE section (" +
            "  Id INTEGER," +
            "  SectionName TEXT," +
            "  ClassId INTEGER," +
            "  TeacherId INTEGER" +
            ")";

    String CREATE_STUDENT = "CREATE TABLE student (" +
            "  Id INTEGER," +
            "  Name TEXT," +
            "  SchoolId INTEGER," +
            "  SectionId INTEGER," +
            "  AdmissionNo TEXT," +
            "  RollNo INTEGER," +
            "  Username TEXT," +
            "  Password TEXT," +
            "  Image TEXT," +
            "  FatherName TEXT," +
            "  MotherName TEXT," +
            "  DateOfBirth date," +
            "  Gender TEXT," +
            "  Email TEXT," +
            "  Mobile1 TEXT," +
            "  Mobile2 TEXT," +
            "  Street TEXT," +
            "  City TEXT," +
            "  District TEXT," +
            "  State TEXT," +
            "  Pincode TEXT" +
            ")";

    String CREATE_SUBJECT = "CREATE TABLE subject (" +
            "  Id INTEGER," +
            "  SchoolId INTEGER," +
            "  SubjectName TEXT," +
            "  PartitionType INTEGER," +
            "  TheorySubjectId INTEGER," +
            "  PracticalSubjectId INTEGER" +
            ")";

    String CREATE_TEACHER = "CREATE TABLE teacher (" +
            "  Id INTEGER," +
            "  Name TEXT," +
            "  Image TEXT," +
            "  Username TEXT," +
            "  Password TEXT," +
            "  SchoolId INTEGER," +
            "  DateOfBirth date," +
            "  Mobile TEXT," +
            "  Qualification TEXT," +
            "  DateOfJoining date," +
            "  Gender TEXT," +
            "  Email TEXT" +
            ")";

    String CREATE_TIMETABLE = "CREATE TABLE timetable (" +
            "  Id INTEGER," +
            "  SectionId INTEGER," +
            "  DayOfWeek TEXT," +
            "  PeriodNo INTEGER," +
            "  SubjectId INTEGER," +
            "  SubjectName TEXT," +
            "  TeacherName TEXT," +
            "  TimingFrom time," +
            "  TimingTo time" +
            ")";

    String CREATE_GROUPS = "CREATE TABLE groups (" +
            " Id INTEGER, " +
            " Name TEXT, " +
            " SectionId INTEGER, " +
            " IsSection INTEGER, " +
            " ClassId INTEGER, " +
            " IsClass INTEGER," +
            " CreatedBy INEGER, " +
            " CreatedDate TEXT, " +
            " IsActive INTEGER" +
            ")";

    String CREATE_CHAT = "CREATE TABLE chat (" +
            " Id INTEGER, " +
            " StudentId INTEGER, " +
            " StudentName TEXT, " +
            " ClassName TEXT, " +
            " SectionName TEXT, " +
            " TeacherId INTEGER, " +
            " TeacherName TEXT, " +
            " CreatedBy INTEGER, " +
            " CreatorRole TEXT" +
            ")";

    String CREATE_MESSAGE = "CREATE TABLE message (" +
            " Id INTEGER, " +
            " SenderId INTEGER, " +
            " SenderRole TEXT, " +
            " SenderName TEXT, " +
            " RecipientId INTEGER, " +
            " RecipientRole TEXT, " +
            " GroupId INTEGER, " +
            " MessageType TEXT, " +
            " MessageBody TEXT, " +
            " ImageUrl TEXT, " +
            " CreatedAt TEXT" +
            ")";

    String CREATE_CHILD_INFO = "CREATE TABLE child_info (" +
            "  SchoolId INTEGER," +
            "  SchoolName TEXT," +
            "  ClassId INTEGER," +
            "  ClassName TEXT," +
            "  SectionId INTEGER," +
            "  SectionName TEXT," +
            "  StudentId INTEGER," +
            "  Name TEXT," +
            "  Image TEXT" +
            ")";

    String CREATE_SERVICE = "CREATE TABLE service (" +
            " Id INTEGER, " +
            " SchoolId INTEGER, " +
            " IsMessage TEXT, " +
            " IsSms TEXT, " +
            " IsChat TEXT, " +
            " IsAttendance TEXT, " +
            " IsAttendanceSms TEXT," +
            " IsHomework TEXT, " +
            " IsHomeworkSms TEXT," +
            " IsTimetable TEXT" +
            ")";

}

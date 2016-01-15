/*  1:   */ package com.health.script;
/*  2:   */ 
/*  3:   */ /*  6:   */ import java.sql.ResultSet;
/*  7:   */ import java.sql.SQLException;
/*  8:   */ import java.sql.Statement;

/* 10:   */ import org.asteriskjava.fastagi.AgiChannel;
/* 11:   */ import org.asteriskjava.fastagi.AgiException;
/* 12:   */ import org.asteriskjava.fastagi.AgiRequest;

import com.agiserver.helper.DatabaseException;
/*  4:   */ import com.agiserver.helper.common.AbstractBaseAgiScript;
/*  5:   */ 
/*  9:   */ 
/* 13:   */ 
/* 14:   */ public class CheckRoomStatus
/* 15:   */   extends AbstractBaseAgiScript
/* 16:   */ {
/* 17:   */   public CheckRoomStatus()
/* 18:   */     throws DatabaseException
/* 19:   */   {}
/* 20:   */   
/* 21:   */   public void service(AgiRequest req, AgiChannel channel)
/* 22:   */     throws AgiException
/* 23:   */   {
/* 24:23 */     String dml = "";
/* 25:24 */     Statement stmt = null;
/* 26:25 */     ResultSet rs = null;
/* 27:   */     try
/* 28:   */     {
/* 29:27 */       dml = "select * from conf_room where now() between conf_room_start_time and conf_room_end_time";
/* 30:   */       
/* 31:   */ 
/* 32:30 */       stmt = super.getConnection().createStatement();
/* 33:31 */       rs = stmt.executeQuery(dml);
/* 34:32 */       if (rs.next())
/* 35:   */       {
/* 36:34 */         channel.setVariable("IS_CONF_ACTIVE", "YES");
/* 37:35 */         channel.setVariable("CONF_ROOM_TYPE", 
/* 38:36 */           rs.getString("conf_room_type"));
/* 39:37 */         channel.setVariable("CONF_WELCOME_FILE", 
/* 40:38 */           rs.getString("welcome_file_name"));
/* 41:39 */         channel.setVariable("CONF_CONNECT_FILE", 
/* 42:40 */           rs.getString("connect_file_name"));
/* 43:41 */         channel.setVariable("CONF_WAIT_FILE", 
/* 44:42 */           rs.getString("wait_file_name"));
/* 45:43 */         channel.setVariable("CONF_ROOM_NAME", 
/* 46:44 */           rs.getString("conf_room_name"));
/* 47:45 */         channel.setVariable("IS_ANNOUNCEMENT_ACTIVE", "NO");
/* 48:46 */         channel.setVariable("IS_NOCELEB_ACTIVE", "NO");
/* 49:47 */         channel.setVariable("IS_CELEBBUSY_ACTIVE", "NO");
/* 50:48 */         channel.setVariable("IS_WAIT_ACTIVE", "NO");
/* 51:   */       }
/* 52:   */       else
/* 53:   */       {
/* 54:51 */         channel.setVariable("IS_CONF_ACTIVE", "NO");
/* 55:   */       }
/* 56:   */     }
/* 57:   */     catch (SQLException e)
/* 58:   */     {
/* 59:54 */       this.logger.error(dml, e);
/* 60:   */       
/* 61:56 */       throw new AgiException("Error setting user vars: " + dml, e);
/* 62:   */     }
/* 63:   */     finally
/* 64:   */     {
/* 65:58 */       if (rs != null) {
/* 66:   */         try
/* 67:   */         {
/* 68:60 */           rs.close();
/* 69:   */         }
/* 70:   */         catch (SQLException localSQLException1) {}
/* 71:   */       }
/* 72:64 */       if (stmt != null) {
/* 73:   */         try
/* 74:   */         {
/* 75:66 */           stmt.close();
/* 76:   */         }
/* 77:   */         catch (SQLException localSQLException2) {}
/* 78:   */       }
/* 79:   */     }
/* 80:   */   }
/* 81:   */ }


/* Location:           C:\Users\mateen\Desktop\Novels\cricket_agi.jar
 * Qualified Name:     com.cricket.script.CheckRoomStatus
 * JD-Core Version:    0.7.0.1
 */
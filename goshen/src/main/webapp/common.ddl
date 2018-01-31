/**********************************/
/* Table Name: 메뉴 */
/**********************************/
CREATE TABLE MENU(
		MENU_ID INT,
		MENU_LV INT,
		MENU_URL VARCHAR(100),
		MENU_NM VARCHAR(50)
);

/**********************************/
/* Table Name: 사용자정보 */
/**********************************/
CREATE TABLE USER_INFO(
		USER_ID VARCHAR(20),
		PASSWORD VARCHAR(12),
		ADMIN_LV INT
);

/**********************************/
/* Table Name: 회사정보 */
/**********************************/
CREATE TABLE COMPANY_INFO(
		REG_NO VARCHAR(10),
		COMPANY_NM VARCHAR(50),
		PHONE VARCHAR(20)
);


ALTER TABLE MENU ADD CONSTRAINT IDX_MENU_PK PRIMARY KEY (MENU_ID, MENU_LV);

ALTER TABLE USER_INFO ADD CONSTRAINT IDX_USER_INFO_PK PRIMARY KEY (USER_ID);


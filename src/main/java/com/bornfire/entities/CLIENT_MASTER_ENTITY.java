package com.bornfire.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
@Table(name="CLIENT_MASTER_TBL")
public class CLIENT_MASTER_ENTITY {
	private String	encoded_key;
	@Id
	private String	customer_id;
	private String	client_state;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDate	creation_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDate	last_modified_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDate	activation_date;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDate	approved_date;
	private String	first_name;
	private String	last_name;
	private String	mobile_phone;
	private String	email_address;
	private String	preferred_language;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDate	birth_date;
	private String	gender;
	private String	assigned_branch_key;
	private String	client_role_key;
	private	BigDecimal loan_cycle;
	private	BigDecimal group_loan_cycle;
	private String	address_line1;
	private String	address_line2;
	private String	address_line3;
	private String	city;
	private String	suburb;
	private String	assigned_user_key;
	@DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDate	asondate;
	public String getEncoded_key() {
		return encoded_key;
	}
	public void setEncoded_key(String encoded_key) {
		this.encoded_key = encoded_key;
	}
	public String getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	public String getClient_state() {
		return client_state;
	}
	public void setClient_state(String client_state) {
		this.client_state = client_state;
	}
	public LocalDate getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(LocalDate creation_date) {
		this.creation_date = creation_date;
	}
	public LocalDate getLast_modified_date() {
		return last_modified_date;
	}
	public void setLast_modified_date(LocalDate last_modified_date) {
		this.last_modified_date = last_modified_date;
	}
	public LocalDate getActivation_date() {
		return activation_date;
	}
	public void setActivation_date(LocalDate activation_date) {
		this.activation_date = activation_date;
	}
	public LocalDate getApproved_date() {
		return approved_date;
	}
	public void setApproved_date(LocalDate approved_date) {
		this.approved_date = approved_date;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getMobile_phone() {
		return mobile_phone;
	}
	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public String getPreferred_language() {
		return preferred_language;
	}
	public void setPreferred_language(String preferred_language) {
		this.preferred_language = preferred_language;
	}
	public LocalDate getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(LocalDate birth_date) {
		this.birth_date = birth_date;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAssigned_branch_key() {
		return assigned_branch_key;
	}
	public void setAssigned_branch_key(String assigned_branch_key) {
		this.assigned_branch_key = assigned_branch_key;
	}
	public String getClient_role_key() {
		return client_role_key;
	}
	public void setClient_role_key(String client_role_key) {
		this.client_role_key = client_role_key;
	}
	public BigDecimal getLoan_cycle() {
		return loan_cycle;
	}
	public void setLoan_cycle(BigDecimal loan_cycle) {
		this.loan_cycle = loan_cycle;
	}
	public BigDecimal getGroup_loan_cycle() {
		return group_loan_cycle;
	}
	public void setGroup_loan_cycle(BigDecimal group_loan_cycle) {
		this.group_loan_cycle = group_loan_cycle;
	}
	public String getAddress_line1() {
		return address_line1;
	}
	public void setAddress_line1(String address_line1) {
		this.address_line1 = address_line1;
	}
	public String getAddress_line2() {
		return address_line2;
	}
	public void setAddress_line2(String address_line2) {
		this.address_line2 = address_line2;
	}
	public String getAddress_line3() {
		return address_line3;
	}
	public void setAddress_line3(String address_line3) {
		this.address_line3 = address_line3;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSuburb() {
		return suburb;
	}
	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}
	public String getAssigned_user_key() {
		return assigned_user_key;
	}
	public void setAssigned_user_key(String assigned_user_key) {
		this.assigned_user_key = assigned_user_key;
	}
	public LocalDate getAsondate() {
		return asondate;
	}
	public void setAsondate(LocalDate asondate) {
		this.asondate = asondate;
	}
	public CLIENT_MASTER_ENTITY(String encoded_key, String customer_id, String client_state, LocalDate creation_date,
			LocalDate last_modified_date, LocalDate activation_date, LocalDate approved_date, String first_name, String last_name,
			String mobile_phone, String email_address, String preferred_language, LocalDate birth_date, String gender,
			String assigned_branch_key, String client_role_key, BigDecimal loan_cycle, BigDecimal group_loan_cycle,
			String address_line1, String address_line2, String address_line3, String city, String suburb,
			String assigned_user_key, LocalDate asondate) {
		super();
		this.encoded_key = encoded_key;
		this.customer_id = customer_id;
		this.client_state = client_state;
		this.creation_date = creation_date;
		this.last_modified_date = last_modified_date;
		this.activation_date = activation_date;
		this.approved_date = approved_date;
		this.first_name = first_name;
		this.last_name = last_name;
		this.mobile_phone = mobile_phone;
		this.email_address = email_address;
		this.preferred_language = preferred_language;
		this.birth_date = birth_date;
		this.gender = gender;
		this.assigned_branch_key = assigned_branch_key;
		this.client_role_key = client_role_key;
		this.loan_cycle = loan_cycle;
		this.group_loan_cycle = group_loan_cycle;
		this.address_line1 = address_line1;
		this.address_line2 = address_line2;
		this.address_line3 = address_line3;
		this.city = city;
		this.suburb = suburb;
		this.assigned_user_key = assigned_user_key;
		this.asondate = asondate;
	}
	public CLIENT_MASTER_ENTITY() {
		super();
		// TODO Auto-generated constructor stub
	}

}

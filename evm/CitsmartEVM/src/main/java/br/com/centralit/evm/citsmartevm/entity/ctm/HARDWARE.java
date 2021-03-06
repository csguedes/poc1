package br.com.centralit.evm.citsmartevm.entity.ctm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "HARDWARE")
@Entity(name="hardware")
//@Table(uniqueConstraints=
//@UniqueConstraint(columnNames = {"NAME", "IPADDR"}))
public class HARDWARE {
	
	@Id
    @Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String PROCESSORT;

	private String NAME;

	private String OSNAME;

	private String SYSTEMDIRECTORY;

	private String DOMAINROLE;

	private String WORKGROUP;

	private String IPADDR;

	private String DESCDOMAINROLE;

	private String LASTBOOT;

	private String OSVERSION;

	private String OSCOMMENTS;

	private String MEMORY;

	private String PROCESSORN;

	private String USERID;
	
	public Long getId() {
		return id;
	}

	@XmlElement
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "HARDWARE [PROCESSORT=" + PROCESSORT + ", NAME=" + NAME
				+ ", OSNAME=" + OSNAME + ", SYSTEMDIRECTORY=" + SYSTEMDIRECTORY
				+ ", DOMAINROLE=" + DOMAINROLE + ", WORKGROUP=" + WORKGROUP
				+ ", IPADDR=" + IPADDR + ", DESCDOMAINROLE=" + DESCDOMAINROLE
				+ ", LASTBOOT=" + LASTBOOT + ", OSVERSION=" + OSVERSION
				+ ", OSCOMMENTS=" + OSCOMMENTS + ", MEMORY=" + MEMORY
				+ ", PROCESSORN=" + PROCESSORN + ", USERID=" + USERID + "]";
	}

	public String getPROCESSORT() {
		return PROCESSORT;
	}

	@XmlElement
	public void setPROCESSORT(String PROCESSORT) {
		this.PROCESSORT = PROCESSORT;
	}

	public String getNAME() {
		return NAME;
	}

	@XmlElement
	public void setNAME(String NAME) {
		this.NAME = NAME;
	}

	public String getOSNAME() {
		return OSNAME;
	}

	@XmlElement
	public void setOSNAME(String OSNAME) {
		this.OSNAME = OSNAME;
	}

	public String getSYSTEMDIRECTORY() {
		return SYSTEMDIRECTORY;
	}

	@XmlElement
	public void setSYSTEMDIRECTORY(String SYSTEMDIRECTORY) {
		this.SYSTEMDIRECTORY = SYSTEMDIRECTORY;
	}

	public String getDOMAINROLE() {
		return DOMAINROLE;
	}

	@XmlElement
	public void setDOMAINROLE(String DOMAINROLE) {
		this.DOMAINROLE = DOMAINROLE;
	}

	public String getWORKGROUP() {
		return WORKGROUP;
	}

	@XmlElement
	public void setWORKGROUP(String WORKGROUP) {
		this.WORKGROUP = WORKGROUP;
	}

	public String getIPADDR() {
		return IPADDR;
	}

	@XmlElement
	public void setIPADDR(String IPADDR) {
		this.IPADDR = IPADDR;
	}

	public String getDESCDOMAINROLE() {
		return DESCDOMAINROLE;
	}

	@XmlElement
	public void setDESCDOMAINROLE(String DESCDOMAINROLE) {
		this.DESCDOMAINROLE = DESCDOMAINROLE;
	}

	public String getLASTBOOT() {
		return LASTBOOT;
	}

	@XmlElement
	public void setLASTBOOT(String LASTBOOT) {
		this.LASTBOOT = LASTBOOT;
	}

	public String getOSVERSION() {
		return OSVERSION;
	}

	@XmlElement
	public void setOSVERSION(String OSVERSION) {
		this.OSVERSION = OSVERSION;
	}

	public String getOSCOMMENTS() {
		return OSCOMMENTS;
	}

	@XmlElement
	public void setOSCOMMENTS(String OSCOMMENTS) {
		this.OSCOMMENTS = OSCOMMENTS;
	}

	public String getMEMORY() {
		return MEMORY;
	}

	@XmlElement
	public void setMEMORY(String MEMORY) {
		this.MEMORY = MEMORY;
	}

	public String getPROCESSORN() {
		return PROCESSORN;
	}

	@XmlElement
	public void setPROCESSORN(String PROCESSORN) {
		this.PROCESSORN = PROCESSORN;
	}

	public String getUSERID() {
		return USERID;
	}

	@XmlElement
	public void setUSERID(String USERID) {
		this.USERID = USERID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IPADDR == null) ? 0 : IPADDR.hashCode());
		result = prime * result + ((NAME == null) ? 0 : NAME.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HARDWARE other = (HARDWARE) obj;
		if (IPADDR == null) {
			if (other.IPADDR != null)
				return false;
		} else if (!IPADDR.equals(other.IPADDR))
			return false;
		if (NAME == null) {
			if (other.NAME != null)
				return false;
		} else if (!NAME.equals(other.NAME))
			return false;
		return true;
	}
	
	
	
	
}

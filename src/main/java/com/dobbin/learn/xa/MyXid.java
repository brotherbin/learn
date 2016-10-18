package com.dobbin.learn.xa;

import javax.transaction.xa.Xid;

public class MyXid implements Xid {

	private int formatId;
	private byte[] gtrid;
	private byte[] bqual;
	
	public MyXid(int formatId, byte[] gtrid, byte[] bqual) {
		this.formatId = formatId;
		this.gtrid = gtrid;
		this.bqual = bqual;
	}

	public byte[] getBranchQualifier() {
		return this.bqual;
	}

	public int getFormatId() {
		return this.formatId;
	}

	public byte[] getGlobalTransactionId() {
		return this.gtrid;
	}

}

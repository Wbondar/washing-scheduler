/**
 * This class is generated by jOOQ
 */
package ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records;


import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.Accounts;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AccountsRecord extends UpdatableRecordImpl<AccountsRecord> implements Record3<Short, Short, Long> {

	private static final long serialVersionUID = 1785865587;

	/**
	 * Setter for <code>public.accounts.id</code>.
	 */
	public void setId(Short value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.accounts.id</code>.
	 */
	public Short getId() {
		return (Short) getValue(0);
	}

	/**
	 * Setter for <code>public.accounts.provider_id</code>.
	 */
	public void setProviderId(Short value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.accounts.provider_id</code>.
	 */
	public Short getProviderId() {
		return (Short) getValue(1);
	}

	/**
	 * Setter for <code>public.accounts.external_id</code>.
	 */
	public void setExternalId(Long value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>public.accounts.external_id</code>.
	 */
	public Long getExternalId() {
		return (Long) getValue(2);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Short> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record3 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Short, Short, Long> fieldsRow() {
		return (Row3) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row3<Short, Short, Long> valuesRow() {
		return (Row3) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Short> field1() {
		return Accounts.ACCOUNTS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Short> field2() {
		return Accounts.ACCOUNTS.PROVIDER_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Long> field3() {
		return Accounts.ACCOUNTS.EXTERNAL_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Short value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Short value2() {
		return getProviderId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long value3() {
		return getExternalId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountsRecord value1(Short value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountsRecord value2(Short value) {
		setProviderId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountsRecord value3(Long value) {
		setExternalId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AccountsRecord values(Short value1, Short value2, Long value3) {
		value1(value1);
		value2(value2);
		value3(value3);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached AccountsRecord
	 */
	public AccountsRecord() {
		super(Accounts.ACCOUNTS);
	}

	/**
	 * Create a detached, initialised AccountsRecord
	 */
	public AccountsRecord(Short id, Short providerId, Long externalId) {
		super(Accounts.ACCOUNTS);

		setValue(0, id);
		setValue(1, providerId);
		setValue(2, externalId);
	}
}

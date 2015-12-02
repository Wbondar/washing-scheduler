/**
 * This class is generated by jOOQ
 */
package ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records;


import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.EffectiveReservations;

import java.sql.Timestamp;

import org.jooq.Field;
import org.jooq.Record7;
import org.jooq.Row7;
import org.jooq.impl.TableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class EffectiveReservationsRecord extends TableRecordImpl<EffectiveReservationsRecord> implements Record7<Integer, Short, Short, Timestamp, Timestamp, Timestamp, Boolean> {

	private static final long serialVersionUID = -906854423;

	/**
	 * Setter for <code>public.effective_reservations.id</code>.
	 */
	public void setId(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>public.effective_reservations.id</code>.
	 */
	public Integer getId() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>public.effective_reservations.machine_id</code>.
	 */
	public void setMachineId(Short value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>public.effective_reservations.machine_id</code>.
	 */
	public Short getMachineId() {
		return (Short) getValue(1);
	}

	/**
	 * Setter for <code>public.effective_reservations.client_id</code>.
	 */
	public void setClientId(Short value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>public.effective_reservations.client_id</code>.
	 */
	public Short getClientId() {
		return (Short) getValue(2);
	}

	/**
	 * Setter for <code>public.effective_reservations.requested_at</code>.
	 */
	public void setRequestedAt(Timestamp value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>public.effective_reservations.requested_at</code>.
	 */
	public Timestamp getRequestedAt() {
		return (Timestamp) getValue(3);
	}

	/**
	 * Setter for <code>public.effective_reservations.locked_at</code>.
	 */
	public void setLockedAt(Timestamp value) {
		setValue(4, value);
	}

	/**
	 * Getter for <code>public.effective_reservations.locked_at</code>.
	 */
	public Timestamp getLockedAt() {
		return (Timestamp) getValue(4);
	}

	/**
	 * Setter for <code>public.effective_reservations.unlocked_at</code>.
	 */
	public void setUnlockedAt(Timestamp value) {
		setValue(5, value);
	}

	/**
	 * Getter for <code>public.effective_reservations.unlocked_at</code>.
	 */
	public Timestamp getUnlockedAt() {
		return (Timestamp) getValue(5);
	}

	/**
	 * Setter for <code>public.effective_reservations.is_canceled</code>.
	 */
	public void setIsCanceled(Boolean value) {
		setValue(6, value);
	}

	/**
	 * Getter for <code>public.effective_reservations.is_canceled</code>.
	 */
	public Boolean getIsCanceled() {
		return (Boolean) getValue(6);
	}

	// -------------------------------------------------------------------------
	// Record7 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row7<Integer, Short, Short, Timestamp, Timestamp, Timestamp, Boolean> fieldsRow() {
		return (Row7) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row7<Integer, Short, Short, Timestamp, Timestamp, Timestamp, Boolean> valuesRow() {
		return (Row7) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return EffectiveReservations.EFFECTIVE_RESERVATIONS.ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Short> field2() {
		return EffectiveReservations.EFFECTIVE_RESERVATIONS.MACHINE_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Short> field3() {
		return EffectiveReservations.EFFECTIVE_RESERVATIONS.CLIENT_ID;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field4() {
		return EffectiveReservations.EFFECTIVE_RESERVATIONS.REQUESTED_AT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field5() {
		return EffectiveReservations.EFFECTIVE_RESERVATIONS.LOCKED_AT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Timestamp> field6() {
		return EffectiveReservations.EFFECTIVE_RESERVATIONS.UNLOCKED_AT;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Boolean> field7() {
		return EffectiveReservations.EFFECTIVE_RESERVATIONS.IS_CANCELED;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Short value2() {
		return getMachineId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Short value3() {
		return getClientId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value4() {
		return getRequestedAt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value5() {
		return getLockedAt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Timestamp value6() {
		return getUnlockedAt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean value7() {
		return getIsCanceled();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EffectiveReservationsRecord value1(Integer value) {
		setId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EffectiveReservationsRecord value2(Short value) {
		setMachineId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EffectiveReservationsRecord value3(Short value) {
		setClientId(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EffectiveReservationsRecord value4(Timestamp value) {
		setRequestedAt(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EffectiveReservationsRecord value5(Timestamp value) {
		setLockedAt(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EffectiveReservationsRecord value6(Timestamp value) {
		setUnlockedAt(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EffectiveReservationsRecord value7(Boolean value) {
		setIsCanceled(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EffectiveReservationsRecord values(Integer value1, Short value2, Short value3, Timestamp value4, Timestamp value5, Timestamp value6, Boolean value7) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		value5(value5);
		value6(value6);
		value7(value7);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached EffectiveReservationsRecord
	 */
	public EffectiveReservationsRecord() {
		super(EffectiveReservations.EFFECTIVE_RESERVATIONS);
	}

	/**
	 * Create a detached, initialised EffectiveReservationsRecord
	 */
	public EffectiveReservationsRecord(Integer id, Short machineId, Short clientId, Timestamp requestedAt, Timestamp lockedAt, Timestamp unlockedAt, Boolean isCanceled) {
		super(EffectiveReservations.EFFECTIVE_RESERVATIONS);

		setValue(0, id);
		setValue(1, machineId);
		setValue(2, clientId);
		setValue(3, requestedAt);
		setValue(4, lockedAt);
		setValue(5, unlockedAt);
		setValue(6, isCanceled);
	}
}

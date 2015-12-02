/**
 * This class is generated by jOOQ
 */
package ch.protonmail.vladyslavbond.washing_scheduler.datasource;


import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.AccountProviders;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.Accounts;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.ContactTypes;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.Contacts;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.MachineComebacks;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.MachineFailures;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.MachineManufacturers;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.MachineModels;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.Machines;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.Requests;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.RequestsCanceled;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.UserAccounts;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.Users;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.AccountProvidersRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.AccountsRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.ContactTypesRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.ContactsRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.MachineComebacksRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.MachineFailuresRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.MachineManufacturersRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.MachineModelsRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.MachinesRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.RequestsCanceledRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.RequestsRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.UserAccountsRecord;
import ch.protonmail.vladyslavbond.washing_scheduler.datasource.tables.records.UsersRecord;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>public</code> 
 * schema
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

	// -------------------------------------------------------------------------
	// IDENTITY definitions
	// -------------------------------------------------------------------------

	public static final Identity<AccountProvidersRecord, Short> IDENTITY_ACCOUNT_PROVIDERS = Identities0.IDENTITY_ACCOUNT_PROVIDERS;
	public static final Identity<AccountsRecord, Short> IDENTITY_ACCOUNTS = Identities0.IDENTITY_ACCOUNTS;
	public static final Identity<ContactsRecord, Short> IDENTITY_CONTACTS = Identities0.IDENTITY_CONTACTS;
	public static final Identity<ContactTypesRecord, Short> IDENTITY_CONTACT_TYPES = Identities0.IDENTITY_CONTACT_TYPES;
	public static final Identity<MachineFailuresRecord, Short> IDENTITY_MACHINE_FAILURES = Identities0.IDENTITY_MACHINE_FAILURES;
	public static final Identity<MachineManufacturersRecord, Short> IDENTITY_MACHINE_MANUFACTURERS = Identities0.IDENTITY_MACHINE_MANUFACTURERS;
	public static final Identity<MachineModelsRecord, Short> IDENTITY_MACHINE_MODELS = Identities0.IDENTITY_MACHINE_MODELS;
	public static final Identity<MachinesRecord, Short> IDENTITY_MACHINES = Identities0.IDENTITY_MACHINES;
	public static final Identity<RequestsRecord, Integer> IDENTITY_REQUESTS = Identities0.IDENTITY_REQUESTS;
	public static final Identity<UsersRecord, Short> IDENTITY_USERS = Identities0.IDENTITY_USERS;

	// -------------------------------------------------------------------------
	// UNIQUE and PRIMARY KEY definitions
	// -------------------------------------------------------------------------

	public static final UniqueKey<AccountProvidersRecord> ACCOUNT_PROVIDERS_PKEY = UniqueKeys0.ACCOUNT_PROVIDERS_PKEY;
	public static final UniqueKey<AccountProvidersRecord> ACCOUNT_PROVIDERS_META_KEY = UniqueKeys0.ACCOUNT_PROVIDERS_META_KEY;
	public static final UniqueKey<AccountsRecord> ACCOUNTS_PKEY = UniqueKeys0.ACCOUNTS_PKEY;
	public static final UniqueKey<AccountsRecord> ACCOUNTS_PROVIDER_ID_EXTERNAL_ID_KEY = UniqueKeys0.ACCOUNTS_PROVIDER_ID_EXTERNAL_ID_KEY;
	public static final UniqueKey<ContactsRecord> CONTACTS_PKEY = UniqueKeys0.CONTACTS_PKEY;
	public static final UniqueKey<ContactsRecord> CONTACTS_CONTENT_KEY = UniqueKeys0.CONTACTS_CONTENT_KEY;
	public static final UniqueKey<ContactTypesRecord> CONTACT_TYPES_PKEY = UniqueKeys0.CONTACT_TYPES_PKEY;
	public static final UniqueKey<ContactTypesRecord> CONTACT_TYPES_META_KEY = UniqueKeys0.CONTACT_TYPES_META_KEY;
	public static final UniqueKey<MachineComebacksRecord> MACHINE_COMEBACKS_PKEY = UniqueKeys0.MACHINE_COMEBACKS_PKEY;
	public static final UniqueKey<MachineFailuresRecord> MACHINE_FAILURES_PKEY = UniqueKeys0.MACHINE_FAILURES_PKEY;
	public static final UniqueKey<MachineFailuresRecord> MACHINE_FAILURES_MACHINE_ID_OCCURED_AT_KEY = UniqueKeys0.MACHINE_FAILURES_MACHINE_ID_OCCURED_AT_KEY;
	public static final UniqueKey<MachineManufacturersRecord> MACHINE_MANUFACTURERS_PKEY = UniqueKeys0.MACHINE_MANUFACTURERS_PKEY;
	public static final UniqueKey<MachineManufacturersRecord> MACHINE_MANUFACTURERS_TITLE_KEY = UniqueKeys0.MACHINE_MANUFACTURERS_TITLE_KEY;
	public static final UniqueKey<MachineModelsRecord> MACHINE_MODELS_PKEY = UniqueKeys0.MACHINE_MODELS_PKEY;
	public static final UniqueKey<MachineModelsRecord> MACHINE_MODELS_CODE_KEY = UniqueKeys0.MACHINE_MODELS_CODE_KEY;
	public static final UniqueKey<MachinesRecord> MACHINES_PKEY = UniqueKeys0.MACHINES_PKEY;
	public static final UniqueKey<RequestsRecord> REQUESTS_PKEY = UniqueKeys0.REQUESTS_PKEY;
	public static final UniqueKey<RequestsCanceledRecord> REQUESTS_CANCELED_PKEY = UniqueKeys0.REQUESTS_CANCELED_PKEY;
	public static final UniqueKey<UserAccountsRecord> USER_ACCOUNTS_PKEY = UniqueKeys0.USER_ACCOUNTS_PKEY;
	public static final UniqueKey<UsersRecord> USERS_PKEY = UniqueKeys0.USERS_PKEY;

	// -------------------------------------------------------------------------
	// FOREIGN KEY definitions
	// -------------------------------------------------------------------------

	public static final ForeignKey<AccountsRecord, AccountProvidersRecord> ACCOUNTS__ACCOUNTS_PROVIDER_ID_FKEY = ForeignKeys0.ACCOUNTS__ACCOUNTS_PROVIDER_ID_FKEY;
	public static final ForeignKey<ContactsRecord, ContactTypesRecord> CONTACTS__CONTACTS_TYPE_ID_FKEY = ForeignKeys0.CONTACTS__CONTACTS_TYPE_ID_FKEY;
	public static final ForeignKey<ContactsRecord, UsersRecord> CONTACTS__CONTACTS_OWNER_ID_FKEY = ForeignKeys0.CONTACTS__CONTACTS_OWNER_ID_FKEY;
	public static final ForeignKey<MachineComebacksRecord, MachineFailuresRecord> MACHINE_COMEBACKS__MACHINE_COMEBACKS_FAILURE_ID_FKEY = ForeignKeys0.MACHINE_COMEBACKS__MACHINE_COMEBACKS_FAILURE_ID_FKEY;
	public static final ForeignKey<MachineFailuresRecord, MachinesRecord> MACHINE_FAILURES__MACHINE_FAILURES_MACHINE_ID_FKEY = ForeignKeys0.MACHINE_FAILURES__MACHINE_FAILURES_MACHINE_ID_FKEY;
	public static final ForeignKey<MachineModelsRecord, MachineManufacturersRecord> MACHINE_MODELS__MACHINE_MODELS_MANUFACTURER_ID_FKEY = ForeignKeys0.MACHINE_MODELS__MACHINE_MODELS_MANUFACTURER_ID_FKEY;
	public static final ForeignKey<MachinesRecord, MachineModelsRecord> MACHINES__MACHINES_MODEL_ID_FKEY = ForeignKeys0.MACHINES__MACHINES_MODEL_ID_FKEY;
	public static final ForeignKey<RequestsCanceledRecord, RequestsRecord> REQUESTS_CANCELED__REQUESTS_CANCELED_REQUEST_ID_FKEY = ForeignKeys0.REQUESTS_CANCELED__REQUESTS_CANCELED_REQUEST_ID_FKEY;
	public static final ForeignKey<RequestsCanceledRecord, UsersRecord> REQUESTS_CANCELED__REQUESTS_CANCELED_CANCELATOR_ID_FKEY = ForeignKeys0.REQUESTS_CANCELED__REQUESTS_CANCELED_CANCELATOR_ID_FKEY;
	public static final ForeignKey<UserAccountsRecord, UsersRecord> USER_ACCOUNTS__USER_ACCOUNTS_USER_ID_FKEY = ForeignKeys0.USER_ACCOUNTS__USER_ACCOUNTS_USER_ID_FKEY;
	public static final ForeignKey<UserAccountsRecord, AccountsRecord> USER_ACCOUNTS__USER_ACCOUNTS_ACCOUNT_ID_FKEY = ForeignKeys0.USER_ACCOUNTS__USER_ACCOUNTS_ACCOUNT_ID_FKEY;

	// -------------------------------------------------------------------------
	// [#1459] distribute members to avoid static initialisers > 64kb
	// -------------------------------------------------------------------------

	private static class Identities0 extends AbstractKeys {
		public static Identity<AccountProvidersRecord, Short> IDENTITY_ACCOUNT_PROVIDERS = createIdentity(AccountProviders.ACCOUNT_PROVIDERS, AccountProviders.ACCOUNT_PROVIDERS.ID);
		public static Identity<AccountsRecord, Short> IDENTITY_ACCOUNTS = createIdentity(Accounts.ACCOUNTS, Accounts.ACCOUNTS.ID);
		public static Identity<ContactsRecord, Short> IDENTITY_CONTACTS = createIdentity(Contacts.CONTACTS, Contacts.CONTACTS.ID);
		public static Identity<ContactTypesRecord, Short> IDENTITY_CONTACT_TYPES = createIdentity(ContactTypes.CONTACT_TYPES, ContactTypes.CONTACT_TYPES.ID);
		public static Identity<MachineFailuresRecord, Short> IDENTITY_MACHINE_FAILURES = createIdentity(MachineFailures.MACHINE_FAILURES, MachineFailures.MACHINE_FAILURES.ID);
		public static Identity<MachineManufacturersRecord, Short> IDENTITY_MACHINE_MANUFACTURERS = createIdentity(MachineManufacturers.MACHINE_MANUFACTURERS, MachineManufacturers.MACHINE_MANUFACTURERS.ID);
		public static Identity<MachineModelsRecord, Short> IDENTITY_MACHINE_MODELS = createIdentity(MachineModels.MACHINE_MODELS, MachineModels.MACHINE_MODELS.ID);
		public static Identity<MachinesRecord, Short> IDENTITY_MACHINES = createIdentity(Machines.MACHINES, Machines.MACHINES.ID);
		public static Identity<RequestsRecord, Integer> IDENTITY_REQUESTS = createIdentity(Requests.REQUESTS, Requests.REQUESTS.ID);
		public static Identity<UsersRecord, Short> IDENTITY_USERS = createIdentity(Users.USERS, Users.USERS.ID);
	}

	private static class UniqueKeys0 extends AbstractKeys {
		public static final UniqueKey<AccountProvidersRecord> ACCOUNT_PROVIDERS_PKEY = createUniqueKey(AccountProviders.ACCOUNT_PROVIDERS, AccountProviders.ACCOUNT_PROVIDERS.ID);
		public static final UniqueKey<AccountProvidersRecord> ACCOUNT_PROVIDERS_META_KEY = createUniqueKey(AccountProviders.ACCOUNT_PROVIDERS, AccountProviders.ACCOUNT_PROVIDERS.META);
		public static final UniqueKey<AccountsRecord> ACCOUNTS_PKEY = createUniqueKey(Accounts.ACCOUNTS, Accounts.ACCOUNTS.ID);
		public static final UniqueKey<AccountsRecord> ACCOUNTS_PROVIDER_ID_EXTERNAL_ID_KEY = createUniqueKey(Accounts.ACCOUNTS, Accounts.ACCOUNTS.PROVIDER_ID, Accounts.ACCOUNTS.EXTERNAL_ID);
		public static final UniqueKey<ContactsRecord> CONTACTS_PKEY = createUniqueKey(Contacts.CONTACTS, Contacts.CONTACTS.ID);
		public static final UniqueKey<ContactsRecord> CONTACTS_CONTENT_KEY = createUniqueKey(Contacts.CONTACTS, Contacts.CONTACTS.CONTENT);
		public static final UniqueKey<ContactTypesRecord> CONTACT_TYPES_PKEY = createUniqueKey(ContactTypes.CONTACT_TYPES, ContactTypes.CONTACT_TYPES.ID);
		public static final UniqueKey<ContactTypesRecord> CONTACT_TYPES_META_KEY = createUniqueKey(ContactTypes.CONTACT_TYPES, ContactTypes.CONTACT_TYPES.META);
		public static final UniqueKey<MachineComebacksRecord> MACHINE_COMEBACKS_PKEY = createUniqueKey(MachineComebacks.MACHINE_COMEBACKS, MachineComebacks.MACHINE_COMEBACKS.FAILURE_ID);
		public static final UniqueKey<MachineFailuresRecord> MACHINE_FAILURES_PKEY = createUniqueKey(MachineFailures.MACHINE_FAILURES, MachineFailures.MACHINE_FAILURES.ID);
		public static final UniqueKey<MachineFailuresRecord> MACHINE_FAILURES_MACHINE_ID_OCCURED_AT_KEY = createUniqueKey(MachineFailures.MACHINE_FAILURES, MachineFailures.MACHINE_FAILURES.MACHINE_ID, MachineFailures.MACHINE_FAILURES.OCCURED_AT);
		public static final UniqueKey<MachineManufacturersRecord> MACHINE_MANUFACTURERS_PKEY = createUniqueKey(MachineManufacturers.MACHINE_MANUFACTURERS, MachineManufacturers.MACHINE_MANUFACTURERS.ID);
		public static final UniqueKey<MachineManufacturersRecord> MACHINE_MANUFACTURERS_TITLE_KEY = createUniqueKey(MachineManufacturers.MACHINE_MANUFACTURERS, MachineManufacturers.MACHINE_MANUFACTURERS.TITLE);
		public static final UniqueKey<MachineModelsRecord> MACHINE_MODELS_PKEY = createUniqueKey(MachineModels.MACHINE_MODELS, MachineModels.MACHINE_MODELS.ID);
		public static final UniqueKey<MachineModelsRecord> MACHINE_MODELS_CODE_KEY = createUniqueKey(MachineModels.MACHINE_MODELS, MachineModels.MACHINE_MODELS.CODE);
		public static final UniqueKey<MachinesRecord> MACHINES_PKEY = createUniqueKey(Machines.MACHINES, Machines.MACHINES.ID);
		public static final UniqueKey<RequestsRecord> REQUESTS_PKEY = createUniqueKey(Requests.REQUESTS, Requests.REQUESTS.ID);
		public static final UniqueKey<RequestsCanceledRecord> REQUESTS_CANCELED_PKEY = createUniqueKey(RequestsCanceled.REQUESTS_CANCELED, RequestsCanceled.REQUESTS_CANCELED.REQUEST_ID);
		public static final UniqueKey<UserAccountsRecord> USER_ACCOUNTS_PKEY = createUniqueKey(UserAccounts.USER_ACCOUNTS, UserAccounts.USER_ACCOUNTS.USER_ID, UserAccounts.USER_ACCOUNTS.ACCOUNT_ID);
		public static final UniqueKey<UsersRecord> USERS_PKEY = createUniqueKey(Users.USERS, Users.USERS.ID);
	}

	private static class ForeignKeys0 extends AbstractKeys {
		public static final ForeignKey<AccountsRecord, AccountProvidersRecord> ACCOUNTS__ACCOUNTS_PROVIDER_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.ACCOUNT_PROVIDERS_PKEY, Accounts.ACCOUNTS, Accounts.ACCOUNTS.PROVIDER_ID);
		public static final ForeignKey<ContactsRecord, ContactTypesRecord> CONTACTS__CONTACTS_TYPE_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.CONTACT_TYPES_PKEY, Contacts.CONTACTS, Contacts.CONTACTS.TYPE_ID);
		public static final ForeignKey<ContactsRecord, UsersRecord> CONTACTS__CONTACTS_OWNER_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.USERS_PKEY, Contacts.CONTACTS, Contacts.CONTACTS.OWNER_ID);
		public static final ForeignKey<MachineComebacksRecord, MachineFailuresRecord> MACHINE_COMEBACKS__MACHINE_COMEBACKS_FAILURE_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.MACHINE_FAILURES_PKEY, MachineComebacks.MACHINE_COMEBACKS, MachineComebacks.MACHINE_COMEBACKS.FAILURE_ID);
		public static final ForeignKey<MachineFailuresRecord, MachinesRecord> MACHINE_FAILURES__MACHINE_FAILURES_MACHINE_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.MACHINES_PKEY, MachineFailures.MACHINE_FAILURES, MachineFailures.MACHINE_FAILURES.MACHINE_ID);
		public static final ForeignKey<MachineModelsRecord, MachineManufacturersRecord> MACHINE_MODELS__MACHINE_MODELS_MANUFACTURER_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.MACHINE_MANUFACTURERS_PKEY, MachineModels.MACHINE_MODELS, MachineModels.MACHINE_MODELS.MANUFACTURER_ID);
		public static final ForeignKey<MachinesRecord, MachineModelsRecord> MACHINES__MACHINES_MODEL_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.MACHINE_MODELS_PKEY, Machines.MACHINES, Machines.MACHINES.MODEL_ID);
		public static final ForeignKey<RequestsCanceledRecord, RequestsRecord> REQUESTS_CANCELED__REQUESTS_CANCELED_REQUEST_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.REQUESTS_PKEY, RequestsCanceled.REQUESTS_CANCELED, RequestsCanceled.REQUESTS_CANCELED.REQUEST_ID);
		public static final ForeignKey<RequestsCanceledRecord, UsersRecord> REQUESTS_CANCELED__REQUESTS_CANCELED_CANCELATOR_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.USERS_PKEY, RequestsCanceled.REQUESTS_CANCELED, RequestsCanceled.REQUESTS_CANCELED.CANCELATOR_ID);
		public static final ForeignKey<UserAccountsRecord, UsersRecord> USER_ACCOUNTS__USER_ACCOUNTS_USER_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.USERS_PKEY, UserAccounts.USER_ACCOUNTS, UserAccounts.USER_ACCOUNTS.USER_ID);
		public static final ForeignKey<UserAccountsRecord, AccountsRecord> USER_ACCOUNTS__USER_ACCOUNTS_ACCOUNT_ID_FKEY = createForeignKey(ch.protonmail.vladyslavbond.washing_scheduler.datasource.Keys.ACCOUNTS_PKEY, UserAccounts.USER_ACCOUNTS, UserAccounts.USER_ACCOUNTS.ACCOUNT_ID);
	}
}

package system.accounts;

public enum AccountOpResult {
	INVALID_PIN_FORMAT,
	INVALID_PIN_WRONG,
	
	INVALID_ACCOUNT_FORMAT, 	// empty input
	INVALID_NO_ACCOUNT,
	INVALID_ACCOUNT_EXISTS,
	
	INVALID_DELETION_NO_ACCS,	// no more accounts to log into
	
	SUCCESS;
}

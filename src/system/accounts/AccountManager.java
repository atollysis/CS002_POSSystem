package system.accounts;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import io.csv.CSVConverter;
import io.csv.CSVManager;
import io.csv.converters.AccountCSVConverter;

public class AccountManager {
	/*
	 * ATTRIBUTES
	 */
	private static final int PIN_MIN_LENGTH = 4;
	private static final String CSV_FILENAME = "accounts.csv";
	private static final Set<Character> INVALID_CHARS = Set.of('"', ',', ';');
	
	private CSVConverter<Account> converter;
	private List<Account> accounts;
	
	private Account currAccount;
	
	/*
	 * CONSTRUCTOR
	 */
	public AccountManager() {
		this.converter = new AccountCSVConverter();
		this.currAccount = null;
		this.loadCSV();
		this.findMaxID();
	}
	
	/*
	 * SUPPORT METHODS
	 */
	private void saveCSV() {
		try {
			CSVManager.saveAll(CSV_FILENAME, this.accounts, this.converter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadCSV() {
		try {
			this.accounts = CSVManager.loadAll(CSV_FILENAME, this.converter);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int findMaxID() {
		return this.accounts.stream()
				.mapToInt(Account::getID)
				.max()
				.orElse(0);
	}
	
	private Account getAccount(String accountName) {
		List<Account> result = this.accounts.stream()
				.filter(acc -> acc.getName().equals(accountName))
				.toList();
		return (result.size() == 1) ? result.get(0) : null;
	}
	
	// Validity checks
	
	private static boolean isValidAccountName(String accountName) {
		if (accountName.isBlank())
			return false;
		if (accountName.chars().anyMatch(ch -> INVALID_CHARS.contains((char) ch)))
			return false;
		
		return true;
	}
	
	private static boolean isValidPin(String pin) {
		if (pin.length() < PIN_MIN_LENGTH)
			return false;
		if (!pin.chars().allMatch(Character::isDigit))
			return false;
		return true;
	}
	
	/*
	 * SERVICE METHODS
	 */
	public String getAccountName(int id) {
		List<String> result = this.accounts.stream()
				.filter(acc -> acc.getID() == id)
				.map   (Account::getName)
				.toList();
		return (result.size() == 1) ? result.get(0) : null;
	}
	
	public AccountOpResult login(String accountName, String pin) {
		if (accountName == null)
			throw new NullPointerException("Account name is null.");
		if (pin == null)
			throw new NullPointerException("Pin is null.");
		
		if (!isValidAccountName(accountName))
			return AccountOpResult.INVALID_ACCOUNT_FORMAT;
		
		Account account = getAccount(accountName);
		if (account == null)
			return AccountOpResult.INVALID_NO_ACCOUNT;
		
		if (!isValidPin(pin))
			return AccountOpResult.INVALID_PIN_FORMAT;
		if (!account.verify(pin))
			return AccountOpResult.INVALID_PIN_WRONG;
		
		this.currAccount = account;
		return AccountOpResult.SUCCESS;
	}
	
	public void logout() {
		this.currAccount = null;
	}
	
	public AccountOpResult createAccount(String accountName, String pin) {
		if (accountName == null)
			throw new NullPointerException("Account name is null.");
		if (pin == null)
			throw new NullPointerException("Pin is null.");
		
		if (!isValidAccountName(accountName))
			return AccountOpResult.INVALID_ACCOUNT_FORMAT;
		if (getAccount(accountName) != null)
			return AccountOpResult.INVALID_ACCOUNT_EXISTS;
		
		if (!isValidPin(pin))
			return AccountOpResult.INVALID_PIN_FORMAT;
		
		this.accounts.add(new Account(
				this.findMaxID() + 1,
				accountName,
				pin));
		
		this.saveCSV();
		return AccountOpResult.SUCCESS;
	}
	
	public AccountOpResult deleteAccount(String accountName, String pin) {
		// Account needs to both exist and the pin should be correct
		AccountOpResult result = login(accountName, pin);
		
		if (result != AccountOpResult.SUCCESS)
			return result;
		if (this.accounts.size() <= 1)
			return AccountOpResult.INVALID_DELETION_NO_ACCS;
		
		this.accounts.remove(this.currAccount);
		this.saveCSV();
		this.logout(); // since it was a successful login beforehand
		return AccountOpResult.SUCCESS;
	}
	
	/*
	 * GETTERS
	 */
	public Account getCurrAccount() {
		return this.currAccount;
	}
	
	// currAccount setter is embedded in the login/logout methods above
}

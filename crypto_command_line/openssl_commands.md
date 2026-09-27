# Install OpenSSL utility on Ubuntu

```bash
sudo apt update
sudo apt install openssl
```

Confirm OpenSSL is installed and available:

```bash
openssl -help
```

```bash
cd crypto
```

# Generate an RSA private key

NOTE: When you create a private key, you essentially create a public/private key pair, the public key is embedded in the private key

```bash
openssl genrsa -out aj-rsa.pem 2048
```

- `openssl`: Runs the OpenSSL command-line utility.
- `genrsa`: Generates an RSA private key. RSA is a public-key cryptographic algorithm named after Rivest, Shamir, and Adleman. The generated private key contains the information needed to derive its matching public key.
- `-out`: Specifies the file where OpenSSL writes the generated key.
- `aj-rsa.pem`: The output filename, created in the current directory. You can choose a different filename or provide a path.
- `.pem`: Indicates a PEM (Privacy-Enhanced Mail) format, a text format containing Base64-encoded data between `-----BEGIN ...-----` and `-----END ...-----` markers. PEM can store keys or certificates; the extension alone does not mean the file contains a certificate or is encrypted.
- `2048`: Sets the RSA key size to 2048 bits. This is the size of the RSA modulus, not the output file size.

This command creates an unencrypted private key because no encryption option is supplied. Keep `aj-rsa.pem` private and do not commit it to version control.

# Inspect an RSA private key

```bash
openssl rsa -text -in aj-rsa.pem -noout
```

- `openssl`: Runs the OpenSSL command-line utility.
- `rsa`: Processes RSA keys. Here, it reads and displays an existing key.
- `-text`: Displays the key's components in a readable form, including its size, modulus, public exponent, and private components. Large values are shown in hexadecimal (base 16).
- `-in`: Specifies the input file to read.
- `aj-rsa.pem`: The PEM-formatted private key file created by the previous command, read from the current directory.
- `-noout`: Suppresses the encoded key output (such as the PEM block). It does not suppress the details requested by `-text`.

The displayed components include:

- **Modulus (`n`)**: The product of the two secret prime numbers; part of both the public and private keys.
- **Public exponent (`e`)**: A value used with the modulus for public-key operations, commonly 65,537.
- **Private exponent (`d`)**: A secret value used for private-key operations, such as signing or decrypting.
- **`prime1` and `prime2` (`p` and `q`)**: The two secret prime numbers used to construct the modulus.
- **`exponent1`, `exponent2`, and `coefficient`**: Secret values derived from the private key that speeds up private-key operations using the Chinese Remainder Theorem (CRT).

This command displays key details in the terminal without modifying the file. The output includes private key material, so keep it private too.

## Extract/print the public key from a rsa private key (essentially the key-pair)

```bash
openssl rsa -in aj-rsa.pem -pubout -text
```
- `-pubout`: publish the public key out in `-text` format

## Save the public key into a file

```bash
openssl rsa -in aj-rsa.pem -pubout -out aj-rsa-pub.pem
```

- `-out` - outputs the public key to the file `aj-rsa-pub.pem`

# Let's test the encryption in action

## Encryption using a public key
```bash
openssl pkeyutl -encrypt -inkey aj-rsa-pub.pem -pubin -in test_data/plain_text.txt -out test_data/encrypted_file.txt
```

- `pkeyutl` - by default expects a private key to use as `-inkey`, but we want to use public key for encryption, so we let it know by passing `pubin` flag

## Decryption using a private key
```bash
openssl pkeyutl -decrypt -inkey aj-rsa.pem -out test_data/decrypted_plain_text.txt -in test_data/encrypted_file.txt
```

- `pkeyutl` - be default expects a private key to use as `-inkey`, but we want to use public key for encryption, so we let it know by passing `pubin` flag

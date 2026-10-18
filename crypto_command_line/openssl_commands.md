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

# Create X509 certificate - public key certificates

A public key on its own says nothing about **who** owns it. An X.509 certificate solves this: it wraps the public key together with identity details (the *subject*, e.g. a name or domain), a validity period and a few other fields, and then **signs** all of it. Anyone holding the certificate can check the signature to confirm the details haven't been tampered with.

Normally a Certificate Authority (CA) signs your certificate. The command below creates a **self-signed** certificate instead: it is signed with your own private key, so you are both the subject and the issuer. That is fine for learning and local development, but browsers and other clients won't trust it automatically.

## Create a self-signed X.509 certificate using SHA-256

```bash
mkdir -p certs
openssl req -x509 -sha256 -days 365 -key aj-rsa.pem -out certs/aj-cert.pem
```

- `mkdir -p certs`: Creates the `certs` folder first; OpenSSL won't create missing folders for `-out`.
- `openssl`: Runs the OpenSSL command-line utility.
- `req`: The certificate *request* tool. On its own it creates a Certificate Signing Request (CSR), which is a file you would send to a CA asking it to sign your public key.
- `-x509`: Skips the CSR and outputs a finished, **self-signed certificate** straight away.
- `-sha256`: Uses the SHA-256 hash algorithm for the signature. OpenSSL hashes the certificate contents with SHA-256 and signs that hash with the private key, giving the signature algorithm `sha256WithRSAEncryption`.
- `-days 365`: The certificate is valid for 365 days from now. After that it expires and clients will reject it.
- `-key aj-rsa.pem`: The private key created earlier. OpenSSL takes the public key out of it to put in the certificate, and uses the private key itself to sign the certificate. The private key is **not** copied into the certificate.
- `-out certs/aj-cert.pem`: Where to write the certificate, in PEM format (`-----BEGIN CERTIFICATE-----`).

When you run it, OpenSSL asks questions to fill in the subject: Country, State, Locality, Organization, Organizational Unit, Common Name and Email. Press Enter to accept a default, or type `.` to leave a field empty. The **Common Name (CN)** matters most: for a server certificate it is usually the host name, such as `localhost`.

To skip the questions, pass the subject on the command line with `-subj`:

```bash
openssl req -x509 -sha256 -days 365 -key aj-rsa.pem -out certs/aj-cert.pem -subj "/C=AU/O=AJ Dev/CN=localhost"
```

On Windows Git Bash, the leading `/` in `-subj` gets turned into a Windows path and the command fails with "subject name is expected to be in the format...". Run `export MSYS_NO_PATHCONV=1` first, or start the value with two slashes: `-subj "//C=AU\O=AJ Dev\CN=localhost"`.

A certificate only contains public information, so unlike `aj-rsa.pem` it is safe to share.

## View the details of an X.509 certificate

```bash
openssl x509 -in certs/aj-cert.pem -text -noout
```

- `openssl`: Runs the OpenSSL command-line utility.
- `x509`: The tool for reading, displaying and converting X.509 certificates.
- `-in certs/aj-cert.pem`: The certificate file to read.
- `-text`: Prints the certificate's contents in a readable form.
- `-noout`: Doesn't print the encoded PEM block again, only the details requested by `-text`.

The main parts of the output are:

- **Version**: The X.509 version, normally `3 (0x2)`. Version 3 allows extensions (see below).
- **Serial Number**: A unique number the issuer gives the certificate. OpenSSL picks a random one here.
- **Signature Algorithm**: How the certificate was signed, here `sha256WithRSAEncryption`, i.e. SHA-256 plus RSA, as chosen by `-sha256`.
- **Issuer**: Who signed the certificate. For a self-signed certificate this is the same as the Subject.
- **Validity**: `Not Before` and `Not After`, the dates between which the certificate is valid. The gap is the 365 days set with `-days`.
- **Subject**: Who the certificate belongs to: the answers you gave to the questions (or the `-subj` value).
- **Subject Public Key Info**: The public key in the certificate: the algorithm (`rsaEncryption`), key size (2048 bits), modulus and exponent. These match the values shown by `openssl rsa -text -in aj-rsa.pem -noout`.
- **X509v3 extensions**: Extra fields. OpenSSL adds these by default:
  - `Subject Key Identifier` / `Authority Key Identifier`: Short IDs of the certificate's own key and of the key that signed it. In a self-signed certificate they are the same.
  - `Basic Constraints: CA:TRUE`: Marks the certificate as a CA certificate, meaning it may be used to sign other certificates.
- **Signature Value**: The signature itself, shown in hexadecimal. Anyone with the issuer's public key can use it to check that the certificate hasn't been altered.

This command only reads the certificate and doesn't change the file.

## Convert an X.509 certificate format (PEM to DER/CER)

Certificate file extensions primarily indicate encoding formats:
- `.pem`: Base64-encoded text enclosed in `-----BEGIN CERTIFICATE-----` headers.
- `.cer` / `.crt` / `.der`: Binary DER-encoded certificate data (or sometimes PEM in Windows environments).

To convert a PEM certificate to a binary DER-encoded certificate:

```bash
openssl x509 -in certs/aj-cert.pem -outform DER -out certs/aj-cert.cer
```
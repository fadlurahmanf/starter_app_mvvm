package com.fadlurahmanf.starterappmvvm.feature.sp.data.storage

import android.content.Context
import com.fadlurahmanf.starterappmvvm.feature.encrypt.data.model.CryptoKey
import com.fadlurahmanf.starterappmvvm.feature.encrypt.presentation.CryptoRSA
import com.fadlurahmanf.starterappmvvm.feature.sp.data.dto.model.ExampleModelSp
import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import com.google.gson.Gson
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ExampleSpStorageTest {
    private lateinit var storage: ExampleSpStorage
    private lateinit var context: Context
    private lateinit var crypto: CryptoRSA

    private fun provideKey(): CryptoKey {
        return CryptoKey(
            privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMRrcrmD6T8nnLAyKSFml20F112e4W1RU4UIL+FER+kAehzJFoBqGQVmu98r5RBScbsygBPt4mNX9D5nW27ukJ9xXCYPdsq0sl6ylPqhPd3mPcJ7Cu2f9tI3iOZeK8Hi1sB5CmSIUUUZzZIcC8mVJztCP/vmbx5FfvgGwkeW73O9AgMBAAECgYABJ6+J361ht4tbL+OecMA06Vt/4iwW5DZy5m9lLDilj8przVMIhSoF2wUfoCmKsx4eJvQivLB2UjUONmqd2ErC4iLKeEf//PErVkb7JUgOkPOdyUS603tp8cD+MgR1qlqUvCN+JiFCphAjZAUwLwOy2ke1UmUYSgqU+127clX44QJBAP27YPxaxkNS00SXgwE5yoEDs+EDsScORC/Os+TJmJbXtf0JTxDieIcCdSluUrcwP4mXkF1dI8i9wyiCw3c8rU0CQQDGLOuYAQ1y9+18Ho5yQuRw7PrY/K6HPvCfTUoqkFf/id/JKbTop4HcsGWeoSiBFV/Oetl2i50p8U/dSQm8F2gxAkBPAOcj7EfLcqChvONnoEzzeumpaHLijEii8iOFW1gNr8DUtu9NfJa2wwckDVvn+jfuaajjyTp5KMz5ci3WlKgJAkBM17YyppXUcpY/fom1fIKf1wQdhz0VbC5ZVtrfTkEQ8SE5G1bur6UHMc71h3/xLZyMr84KzXAPh2Z0Y5HGwTzBAkEArmrd4tcPDXUEwCY0YgsNBwC8nv3NJRrdIESc1iHAIwcLbec5QJFO51xf5xF72zWzuoDhTCCTD+EB5RJTgD9wBg==",
            publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEa3K5g+k/J5ywMikhZpdtBdddnuFtUVOFCC/hREfpAHocyRaAahkFZrvfK+UQUnG7MoAT7eJjV/Q+Z1tu7pCfcVwmD3bKtLJespT6oT3d5j3Cewrtn/bSN4jmXivB4tbAeQpkiFFFGc2SHAvJlSc7Qj/75m8eRX74BsJHlu9zvQIDAQAB"
        )
    }

    @Before
    fun before() {
        context = SPMockBuilder().createContext()
        crypto = mock<CryptoRSA>()
        whenever(crypto.generateKey()).thenReturn(provideKey())

        storage = ExampleSpStorage(context, crypto)
    }

    @Test
    fun `save & get string`() {
        storage.exampleRawString = "EXAMPLE RAW STRING"
        Assertions.assertEquals("EXAMPLE RAW STRING", storage.exampleRawString)
    }

    @Test
    fun `save & get encrypted string`() {
        whenever(
            crypto.encrypt(
                "EXAMPLE STRING",
                provideKey().publicKey
            )
        ).thenReturn("ENCRYPTED_STRING")
        whenever(
            crypto.decrypt(
                "ENCRYPTED_STRING",
                provideKey().privateKey
            )
        ).thenReturn("EXAMPLE STRING")
        storage.exampleString = "EXAMPLE STRING"

        verify(crypto, times(1)).encrypt("EXAMPLE STRING", provideKey().publicKey)

        Assertions.assertEquals("EXAMPLE STRING", storage.exampleString)

        verify(crypto, times(1)).decrypt("ENCRYPTED_STRING", provideKey().privateKey)
    }

    @Test
    fun `save & get encrypted int`() {
        whenever(
            crypto.encrypt(
                "1",
                provideKey().publicKey
            )
        ).thenReturn("ENCRYPTED_STRING")
        whenever(crypto.decrypt("ENCRYPTED_STRING", provideKey().privateKey)).thenReturn("1")
        storage.exampleInt = 1

        verify(crypto, times(1)).encrypt("1", provideKey().publicKey)

        Assertions.assertEquals(1, storage.exampleInt)

        verify(crypto, times(1)).decrypt("ENCRYPTED_STRING", provideKey().privateKey)
    }

    @Test
    fun `save & get encrypted long`() {
        whenever(
            crypto.encrypt(
                (1L).toString(),
                provideKey().publicKey
            )
        ).thenReturn("ENCRYPTED_STRING")
        whenever(
            crypto.decrypt(
                "ENCRYPTED_STRING",
                provideKey().privateKey
            )
        ).thenReturn((1L).toString())
        storage.exampleLong = 1L

        verify(crypto, times(1)).encrypt((1L).toString(), provideKey().publicKey)

        Assertions.assertEquals(1L, storage.exampleLong)

        verify(crypto, times(1)).decrypt("ENCRYPTED_STRING", provideKey().privateKey)
    }

    @Test
    fun `save & get encrypted float`() {
        whenever(
            crypto.encrypt(
                (1f).toString(),
                provideKey().publicKey
            )
        ).thenReturn("ENCRYPTED_STRING")
        whenever(
            crypto.decrypt(
                "ENCRYPTED_STRING",
                provideKey().privateKey
            )
        ).thenReturn((1f).toString())
        storage.exampleFloat = 1f

        verify(crypto, times(1)).encrypt((1f).toString(), provideKey().publicKey)

        Assertions.assertEquals(1f, storage.exampleFloat)

        verify(crypto, times(1)).decrypt("ENCRYPTED_STRING", provideKey().privateKey)
    }

    @Test
    fun `save & get encrypted data`() {
        whenever(
            crypto.encrypt(
                Gson().toJson(
                    ExampleModelSp(
                        string = "EXAMPLE STRING",
                        int = 150,
                        float = 4f,
                        long = 2L
                    )
                ),
                provideKey().publicKey
            )
        ).thenReturn("ENCRYPTED_STRING")
        whenever(
            crypto.decrypt(
                "ENCRYPTED_STRING",
                provideKey().privateKey
            )
        ).thenReturn(
            (Gson().toJson(
                ExampleModelSp(
                    string = "EXAMPLE STRING",
                    int = 150,
                    float = 4f,
                    long = 2L
                )
            )).toString()
        )
        storage.exampleData = ExampleModelSp(
            string = "EXAMPLE STRING",
            int = 150,
            float = 4f,
            long = 2L
        )

        verify(crypto, times(1)).encrypt(
            (Gson().toJson(
                ExampleModelSp(
                    string = "EXAMPLE STRING",
                    int = 150,
                    float = 4f,
                    long = 2L
                )
            )).toString(), provideKey().publicKey
        )

        Assertions.assertEquals(
            ExampleModelSp(
                string = "EXAMPLE STRING",
                int = 150,
                float = 4f,
                long = 2L
            ), storage.exampleData
        )

        verify(crypto, times(1)).decrypt("ENCRYPTED_STRING", provideKey().privateKey)
    }

    @Test
    fun `save & get encrypted list data`() {
        whenever(
            crypto.encrypt(
                Gson().toJson(
                    arrayListOf(
                        ExampleModelSp(
                            string = "EXAMPLE STRING",
                            int = 150,
                            float = 4f,
                            long = 2L
                        ),
                        ExampleModelSp(
                            string = "EXAMPLE STRING",
                            int = 150,
                            float = 4f,
                            long = 2L
                        )
                    )
                ),
                provideKey().publicKey
            )
        ).thenReturn("ENCRYPTED_STRING")

        whenever(
            crypto.decrypt(
                "ENCRYPTED_STRING",
                provideKey().privateKey
            )
        ).thenReturn(
            Gson().toJson(
                arrayListOf(
                    ExampleModelSp(
                        string = "EXAMPLE STRING",
                        int = 150,
                        float = 4f,
                        long = 2L
                    ),
                    ExampleModelSp(
                        string = "EXAMPLE STRING",
                        int = 150,
                        float = 4f,
                        long = 2L
                    )
                )
            )
        )

        storage.exampleListData = arrayListOf(
            ExampleModelSp(
                string = "EXAMPLE STRING",
                int = 150,
                float = 4f,
                long = 2L
            ),
            ExampleModelSp(
                string = "EXAMPLE STRING",
                int = 150,
                float = 4f,
                long = 2L
            )
        )

        verify(crypto, times(1)).encrypt(
            (Gson().toJson(
                arrayListOf(
                    ExampleModelSp(
                        string = "EXAMPLE STRING",
                        int = 150,
                        float = 4f,
                        long = 2L
                    ),
                    ExampleModelSp(
                        string = "EXAMPLE STRING",
                        int = 150,
                        float = 4f,
                        long = 2L
                    )
                )
            )).toString(), provideKey().publicKey
        )

        Assertions.assertEquals(
            arrayListOf(
                ExampleModelSp(
                    string = "EXAMPLE STRING",
                    int = 150,
                    float = 4f,
                    long = 2L
                ),
                ExampleModelSp(
                    string = "EXAMPLE STRING",
                    int = 150,
                    float = 4f,
                    long = 2L
                )
            ), storage.exampleListData
        )

        verify(crypto, times(1)).decrypt("ENCRYPTED_STRING", provideKey().privateKey)
    }
}
package com.example.currencyconverter.data.database

import com.example.currencyconverter.data.toDomainCurrency
import com.example.currencyconverter.data.toRoomCurrency
import com.example.data.source.LocalDataSource
import com.example.domain.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(db: CurrencyDatabase) : LocalDataSource {

    private val currencyDao = db.currencyDao()
    private val rateDao = db.rateDao()

    override suspend fun saveCurrencies(currencies: List<Currency>) {
        withContext(Dispatchers.IO) { currencyDao.insertCurrencies(currencies.map { it.toRoomCurrency() }) }
    }

    override suspend fun getLatestCurrencies(): List<Currency> =
        currencyDao.getAll().map { it.toDomainCurrency() } as MutableList<Currency>

    override suspend fun update(currency: Currency) {
        withContext(Dispatchers.IO) { currencyDao.updateCurrency(currency.toRoomCurrency()) }
    }

    override suspend fun isEmpty(): Boolean  =
        withContext(Dispatchers.IO) { currencyDao.currencyCount() <= 0 }

    override suspend fun findById(id: Int): Currency = withContext(Dispatchers.IO) {
        currencyDao.findById(id).toDomainCurrency()
    }
}
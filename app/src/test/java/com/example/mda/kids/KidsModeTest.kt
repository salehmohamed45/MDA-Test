package com.example.mda.kids

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.mda.data.datastore.KidsSecurityDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class KidsModeTest {

    private lateinit var context: Context
    private lateinit var kidsSecurityStore: KidsSecurityDataStore

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        kidsSecurityStore = KidsSecurityDataStore(context)
    }

    @Test
    fun testKidsModeActivation() = runBlocking {
        kidsSecurityStore.setActive(true)
        val isActive = kidsSecurityStore.activeFlow.first()
        assertTrue(isActive)
    }

    @Test
    fun testKidsModeDeactivation() = runBlocking {
        kidsSecurityStore.setActive(true)
        kidsSecurityStore.setActive(false)
        val isActive = kidsSecurityStore.activeFlow.first()
        assertFalse(isActive)
    }

    @Test
    fun testKidsModeDefaultInactive() = runBlocking {
        val isActive = kidsSecurityStore.activeFlow.first()
        assertFalse(isActive)
    }

    @Test
    fun testKidsModePersistence() = runBlocking {
        kidsSecurityStore.setActive(true)
        
        val newStore = KidsSecurityDataStore(context)
        val isActive = newStore.activeFlow.first()
        assertTrue(isActive)
    }

    @Test
    fun testKidsModeWithLockEnabled() = runBlocking {
        kidsSecurityStore.setLockEnabled(true)
        kidsSecurityStore.setPin("123456")
        kidsSecurityStore.setActive(true)
        
        val lockEnabled = kidsSecurityStore.lockEnabledFlow.first()
        val pin = kidsSecurityStore.pinFlow.first()
        val isActive = kidsSecurityStore.activeFlow.first()
        
        assertTrue(lockEnabled)
        assertEquals("123456", pin)
        assertTrue(isActive)
    }

    @Test
    fun testKidsModeWithLockDisabled() = runBlocking {
        kidsSecurityStore.setLockEnabled(false)
        kidsSecurityStore.setActive(true)
        
        val lockEnabled = kidsSecurityStore.lockEnabledFlow.first()
        val isActive = kidsSecurityStore.activeFlow.first()
        
        assertFalse(lockEnabled)
        assertTrue(isActive)
    }

    @Test
    fun testKidsModeExitWithCorrectPin() = runBlocking {
        val testPin = "123456"
        kidsSecurityStore.setPin(testPin)
        kidsSecurityStore.setLockEnabled(true)
        kidsSecurityStore.setActive(true)
        
        val savedPin = kidsSecurityStore.pinFlow.first()
        val enteredPin = "123456"
        
        if (enteredPin.trim() == savedPin?.trim()) {
            kidsSecurityStore.setActive(false)
        }
        
        val isActive = kidsSecurityStore.activeFlow.first()
        assertFalse(isActive)
    }

    @Test
    fun testKidsModeExitWithIncorrectPin() = runBlocking {
        val testPin = "123456"
        kidsSecurityStore.setPin(testPin)
        kidsSecurityStore.setLockEnabled(true)
        kidsSecurityStore.setActive(true)
        
        val savedPin = kidsSecurityStore.pinFlow.first()
        val enteredPin = "654321"
        
        if (enteredPin.trim() == savedPin?.trim()) {
            kidsSecurityStore.setActive(false)
        }
        
        val isActive = kidsSecurityStore.activeFlow.first()
        assertTrue(isActive)
    }

    @Test
    fun testKidsModeExitWithoutLock() = runBlocking {
        kidsSecurityStore.setLockEnabled(false)
        kidsSecurityStore.setActive(true)
        
        kidsSecurityStore.setActive(false)
        
        val isActive = kidsSecurityStore.activeFlow.first()
        assertFalse(isActive)
    }

    @Test
    fun testKidsModeWithSecurityQuestions() = runBlocking {
        kidsSecurityStore.setActive(true)
        kidsSecurityStore.setSecurityQA(0, 1, 2, "Answer 1", "Answer 2", "Answer 3")
        
        val isActive = kidsSecurityStore.activeFlow.first()
        val qa = kidsSecurityStore.securityQAFlow.first()
        
        assertTrue(isActive)
        assertNotNull(qa.q1)
        assertNotNull(qa.q2)
        assertNotNull(qa.q3)
    }

    @Test
    fun testKidsModeToggle() = runBlocking {
        kidsSecurityStore.setActive(true)
        var isActive = kidsSecurityStore.activeFlow.first()
        assertTrue(isActive)
        
        kidsSecurityStore.setActive(false)
        isActive = kidsSecurityStore.activeFlow.first()
        assertFalse(isActive)
        
        kidsSecurityStore.setActive(true)
        isActive = kidsSecurityStore.activeFlow.first()
        assertTrue(isActive)
    }

    @Test
    fun testKidsModeWithLegacyPin() = runBlocking {
        val legacyPin = "1234"
        kidsSecurityStore.setPin(legacyPin)
        kidsSecurityStore.setActive(true)
        
        val savedPin = kidsSecurityStore.pinFlow.first()
        val requiredLength = (savedPin?.length ?: 6).coerceIn(4, 6)
        
        assertEquals(4, requiredLength)
        assertEquals(legacyPin, savedPin)
    }

    @Test
    fun testKidsModeWithModernPin() = runBlocking {
        val modernPin = "123456"
        kidsSecurityStore.setPin(modernPin)
        kidsSecurityStore.setActive(true)
        
        val savedPin = kidsSecurityStore.pinFlow.first()
        val requiredLength = (savedPin?.length ?: 6).coerceIn(4, 6)
        
        assertEquals(6, requiredLength)
        assertEquals(modernPin, savedPin)
    }

    @Test
    fun testKidsModeCompleteFlow() = runBlocking {
        kidsSecurityStore.setPin("123456")
        kidsSecurityStore.setLockEnabled(true)
        kidsSecurityStore.setSecurityQA(0, 1, 2, "A1", "A2", "A3")
        
        kidsSecurityStore.setActive(true)
        var isActive = kidsSecurityStore.activeFlow.first()
        assertTrue(isActive)
        
        val pin = kidsSecurityStore.pinFlow.first()
        val lockEnabled = kidsSecurityStore.lockEnabledFlow.first()
        val qa = kidsSecurityStore.securityQAFlow.first()
        
        assertEquals("123456", pin)
        assertTrue(lockEnabled)
        assertEquals(0, qa.q1)
        
        kidsSecurityStore.setActive(false)
        isActive = kidsSecurityStore.activeFlow.first()
        assertFalse(isActive)
    }
}

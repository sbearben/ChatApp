package uk.co.victoriajanedavis.chatapp.common

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

// FIXME: This class should be in base module but the project can't compile when is placed there.
// Move when the problem with tests and modules is solved.
class RxSchedulerOverrideRule : TestRule {

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxJavaPlugins.setIoSchedulerHandler { _ -> Schedulers.trampoline() }
                RxJavaPlugins.setComputationSchedulerHandler { _ -> Schedulers.trampoline() }
                RxJavaPlugins.setNewThreadSchedulerHandler { _ -> Schedulers.trampoline() }
                try {
                    base.evaluate()
                } finally {
                    RxJavaPlugins.reset()
                }
            }
        }
    }
}

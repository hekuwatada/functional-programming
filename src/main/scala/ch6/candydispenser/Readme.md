Exercise 6.11 - Purely functional state

Actions

* insert a coin
* turn the knob to dispense candy

States

* locked
* unlocked

Rules

* Inserting a coin into a locked machine will cause it to unlock if there is any candy left.
* Turning the knob on an unlocked machine will cause it to dispense candy and become locked.
* Turning the knob on a locked machine or inserting a coin into an unlocked machine does nothing.
* A machine that is out of candy ignores all inputs.

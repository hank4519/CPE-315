#   CPE 315

# Name: Hank Tsai. Partner name: Liam Shaw 
# Section: 01 
# Description: This is a program that takes in an integer, and returns the number with the reversed binary representation of that number. 

# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl reverse

.data
welcome:
	.asciiz " This program adds two numbers \n\n"

prompt:
	.asciiz " Enter an integer: "

reverse:
    .asciiz " The reversed number is: "

.text

main:

	ori     $v0, $0, 4
	lui     $a0, 0x1001
	syscall

	ori     $v0, $0, 4
	lui     $a0, 0x1001
    ori     $a0, $a0,0x22
    syscall

    ori     $v0, $0, 5
    syscall
    add $t0, $v0, $0

    # Clear $s0 for the result
    ori     $s0, $0, 0
    ori     $s1, $0, 0

    add $s1, $0, 1

    and $t1, $t0, $s1
    or $s0, $t1, $s0

    #s0 *=2
    sll $s0, $s0, 1

    #t0 /=2
    srl $t0, $t0, 1

    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1

    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1

    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1

    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1

    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1

    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1
    and $t1, $t0, $s1
        or $s0, $t1, $s0
        sll $s0, $s0, 1
        srl $t0, $t0, 1

    ori     $v0, $0, 4
    lui     $a0, 0x1001
    ori     $a0, $a0,0x37
    syscall

    ori     $v0, $0, 1
    add 	$a0, $s0, $0
    syscall

    #ori     $v0, $0, 1
    #add 	$a0, $s0, $0
    #syscall

    # Exit (load 10 into $v0)
    ori     $v0, $0, 10
    syscall




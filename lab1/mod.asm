#   CPE 315
# My name: Hank Tsai; My partner: Liam Shaw
# Section: 01
# Description: This program takes in an integer, and a divisor that's a power of two. And it will perform modulus operation/ 

# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl remainderText
.globl divisorText

#  Data Area (this area contains strings to be displayed during the program)
.data

welcome:
	.asciiz " This program adds two numbers \n\n"
#35
prompt:
	.asciiz " Enter an integer: "
#54
divisorText:
	.asciiz "  Enter a divisor: "
#72
remainderText:
    .asciiz " The remainder =  "

#Text Area (i.e. instructions)
.text

main:

	# Display the welcome message (load 4 into $v0 to display)
	ori     $v0, $0, 4

	# This generates the starting address for the welcome message.
	# (assumes the register first contains 0).
	lui     $a0, 0x1001
	syscall

	# Display prompt
	ori     $v0, $0, 4

	# This is the starting address of the prompt (notice the
	# different address from the welcome message)
	lui     $a0, 0x1001
	ori     $a0, $a0,0x22
	syscall

	# Read 1st integer from the user (5 is loaded into $v0, then a syscall)
	ori     $v0, $0, 5
	syscall

	# Clear $s0 for the remainder
	ori     $s0, $0, 0

	add $t0, $v0, $0


	# Display prompt (4 is loaded into $v0 to display)
	# 0x22 is hexidecimal for 34 decimal (the length of the previous welcome message)
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori     $a0, $a0,0x37
	syscall

	# Read 2nd integer
	ori	$v0, $0, 5
	syscall
	addi $s0, $v0, -1
	# $v0 now has the value of the second integer

    and $s0, $s0, $t0


	# Display the sum text
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	ori     $a0, $a0,0x4A
	syscall

	# Display the sum
	# load 1 into $v0 to display an integer
	ori     $v0, $0, 1
	add 	$a0, $s0, $0
	syscall

	# Exit (load 10 into $v0)
	ori     $v0, $0, 10
	syscall


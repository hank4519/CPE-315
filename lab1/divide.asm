#   CPE 315

#Name: Hank Tsai   Partner name: Liam Shaw 
#Section: 01
#Description: Take in two numbers as upper and lower, and a vidisor. 
# Then represent a 64 bits with upper and lower, and divide it by a 31 bitsnumber. 


# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl prompt2
.globl prompt3


.data
welcome:
	.asciiz " This program divides two numbers \n\n"

prompt:
	.asciiz " Enter the upper integer: "

#64
prompt2:
    .asciiz " Enter the lower integer: "

#90
prompt3:
    .asciiz " Enter the divisor: "
#111
final:
    .asciiz " Final upper = "
#126
final2:
    .asciiz " Final lower = "
.text

main:
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	syscall

	ori     $v0, $0, 4
    lui     $a0, 0x1001
    ori     $a0, $a0,0x25
    syscall

    #upper $s0
    ori     $v0, $0, 5
    syscall
    add $s0, $v0, $0

    #lower $s1
    ori     $v0, $0, 4
    lui     $a0, 0x1001
    ori     $a0, $a0,0x40
    syscall
    ori     $v0, $0, 5
    syscall
    add $s1, $v0, $0

    #divisor $v1
    ori     $v0, $0, 4
    lui     $a0, 0x1001
    ori     $a0, $a0,0x5B
    syscall
    ori     $v0, $0, 5
    syscall
    add $v1, $v0, $0

    loop:
        slt $t1, $v1, 2
        beq $t1, 1, end
        srl $s1, $s1, 1
        addi $s2, $0, 1
        and $s2, $s0, $s2
        srl $s0, $s0, 1
        sll $s2, $s2, 31
        or $s1, $s1, $s2
        srl $v1, $v1, 1
        j loop
    end:

        ori     $v0, $0, 4
        lui     $a0, 0x1001
        ori     $a0, $a0,0x70
        syscall

        ori     $v0, $0, 1
        add 	$a0, $s0, $0
        syscall

        ori     $v0, $0, 4
        lui     $a0, 0x1001
        ori     $a0, $a0,0x80
        syscall

        ori     $v0, $0, 1
        add 	$a0, $s1, $0
        syscall

    # Exit (load 10 into $v0)
    ori     $v0, $0, 10
    syscall





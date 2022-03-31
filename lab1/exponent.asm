
#Name: Hank Tsai; Partner Name: Liam Shaw
#Section: 01 
#Description: This program performs an exponent operation. 


# x = x ^ y
# declare global so programmer can see actual addresses.
.globl welcome
.globl prompt
.globl prompt2
.globl result


.data
welcome:
	.asciiz " This program divides two numbers \n\n"

# 38
prompt:
	.asciiz " Enter the base: "

#56
prompt2:
    .asciiz " Enter the exponent: "

#77
result:
    .asciiz "The answer is = "

.text

main:
	ori     $v0, $0, 4
	lui     $a0, 0x1001
	syscall

	ori     $v0, $0, 4
    lui     $a0, 0x1001
    ori     $a0, $a0,0x26
    syscall

    # s0=base
    ori     $v0, $0, 5
    syscall
    add $s0, $v0, $0
    add $t5, $s0, $0

    ori     $v0, $0, 4
    lui     $a0, 0x1001
    ori     $a0, $a0,0x38
    syscall

    #s1=exponent
    ori     $v0, $0, 5
    syscall
    add $s1, $v0, $0
    addi $t3, $0, 2
    addi $t6, $s1, 1
    loop:
    slt $t1, $t3, $t6
    beq $t1, 0, end
    ori $t4, $0, 0
    addi $t4, $t4, 1
    ori $t7, $0, 0
    add $t7, $0, $s0
        innerloop:
        slt $t2, $t4, $t5
        beq $t2, 0, innerend
        add $s0, $s0, $t7
        addi $t4, $t4, 1
        j innerloop
        innerend:
    addi $t3, $t3, 1
    j loop
    end:

    ori     $v0, $0, 4
    lui     $a0, 0x1001
    ori     $a0, $a0,0x4D
    syscall

    ori     $v0, $0, 1
    add 	$a0, $s0, $0
    syscall

# Exit (load 10 into $v0)
    ori     $v0, $0, 10
    syscall



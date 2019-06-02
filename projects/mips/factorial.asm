	.data
	INT1:	.asciiz	"Enter 0 and 0 to exit.\nEnter integer n (0<=n<=30) => "
	INT2:	.asciiz	"Enter integer k (0<=k<=30 and k<n) => "
	ANS:	.asciiz	"Answer: "
	NLINE:	.asciiz "\n\n"
	
	.text	
	.globl main
main:	

	li 	$v0, 4		# syscall to print string INT1
	la	$a0, INT1
	syscall
	
	li 	$v0, 5		# syscall to read int n
	syscall
	move 	$t7, $v0	# temporarily store into $t7 to free up $a0
	
	li	$v0, 4		# syscall to print string INT2
	la 	$a0, INT2
	syscall
	
	li 	$v0, 5		# syscall to read int k
	syscall
	move 	$a1, $v0	# store into $a1
	move	$a0, $t7	# move n back to $a0
	
	li	$t0, 1		# $t0 contains constant integer 1
	seq	$t1, $a0, $zero	# $t1 is 1 if n=0
	seq	$t2, $a1, $zero	# $t2 is 1 if k=0
	and	$t3, $t1, $t2	# $t3 contains 1 if both n=k=0
	beq	$t3, $t0, end	# branch to end if n=k=0
	
	move	$v1, $zero	# reset answer counter
	jal	c		# call function
	
	li	$v0, 4		# syscall to print string ANS
	la	$a0, ANS
	syscall
	
	move	$a0, $v1	# syscall to print int answer
	li	$v0, 1
	syscall
	
	li	$v0, 4		# syscall to print string NLINE
	la	$a0, NLINE
	syscall
	
	j	main		# go back to beginning

c:
	addi 	$sp, $sp, -12	# allocate room for 3 variables
	sw	$a0, 0($sp)	# first argument: n
	sw	$a1, 4($sp)	# second argument: k
	sw	$ra, 8($sp)	# save a return address
	
	beq	$a0, $a1, base	# if n=k then go to base case
	
	sgt	$t1, $a0, $zero	# $t1 is 1 if n>0
	seq	$t2, $a1, $zero	# $t2 is 1 if k=0
	and	$t3, $t1, $t2	# $t3 contains 1 if both n>0 and k=0
	beq	$t3, $t0, base	# if $t3=1 then go to base case
	
	j	rec		# else need to recurse further
	
base:	addi	$v1, $v1, 1	# increment answer
	addi	$sp, $sp, 12	# pop 3 items off stack
	jr	$ra		# return to caller
	
rec:	lw	$a0, 0($sp)	# reload n
	addi	$a0, $a0, -1	# n--
	jal	c		# call C(n-1, k)
	
	lw	$a0, 0($sp)	# reload n
	addi	$a0, $a0, -1	# n--
	lw	$a1, 4($sp)	# reload k
	addi	$a1, $a1, -1	# k--
	jal	c		# call C(n-1, k-1)
	
	lw	$a0, 0($sp)	# restore original n
	lw	$a1, 4($sp)	# restore original k
	lw	$ra, 8($sp)	# restore original return address
	addi	$sp, $sp, 12	# pop 3 items off stack
	jr 	$ra		# return out of function

end:	li	$v0, 10		# syscall 10 to exit program
	syscall
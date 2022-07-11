package com.copperleaf.json.utils

public sealed class Either<out LeftT, out RightT> {
    public data class Left<LeftT>(val value: LeftT) : Either<LeftT, Nothing>()
    public data class Right<RightT>(val value: RightT) : Either<Nothing, RightT>()
}

// Creation functions
// ---------------------------------------------------------------------------------------------------------------------

public fun <L, R> L.asLeft(): Either<L, R> {
    return Either.Left(this)
}

public fun <L, R> R.asRight(): Either<L, R> {
    return Either.Right(this)
}

public inline fun <reified L, reified R> Any.asEither(): Either<L, R> {
    return if (this is L) {
        this.asLeft()
    } else if (this is R) {
        this.asRight()
    } else {
        error("$this is neither ${L::class.simpleName} nor ${R::class.simpleName}")
    }
}

// Access values
// ---------------------------------------------------------------------------------------------------------------------

public fun <L, R> Either<L, R>.requireLeftValue(): L {
    return (this as Either.Left<L>).value
}

public val <L, R> Either<L, R>.leftValue: L?
    get() {
        return (this as? Either.Left<L>)?.value
    }

public fun <L, R> Either<L, R>.requireRightValue(): R {
    return (this as Either.Right<R>).value
}

public val <L, R> Either<L, R>.rightValue: R?
    get() {
        return (this as? Either.Right<R>)?.value
    }

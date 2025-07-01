import android.view.View


fun View.setVisibleOrGone(isVisible: Boolean) {
    this.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun View.setVisibleOrInvisible(isVisible: Boolean) {
    this.visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}
package com.joeblakeb.battleships.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.joeblakeb.battleships.R
import com.joeblakeb.battleships.utils.EXTRA_OTHER_PLAYER
import com.joeblakeb.battleships.utils.OTHER_PLAYER_PROBABILITY
import com.joeblakeb.battleships.utils.OTHER_PLAYER_RANDOM

/**
 * The initial activity which opens when the app is opened.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpButtons()
        setUpFooterLinks()
    }

    private fun setUpButtons() {
        for (pair in listOf(
            Pair(R.id.buttonPlayEasy, OTHER_PLAYER_RANDOM),
            Pair(R.id.buttonPlayHard, OTHER_PLAYER_PROBABILITY)
        )) {
            val button = findViewById<Button>(pair.first)
            button.setOnClickListener {
                val intent = Intent(this, PlaceShipsActivity::class.java)
                intent.putExtra(EXTRA_OTHER_PLAYER, pair.second)
                startActivity(intent)
            }
        }
    }

    private fun setUpFooterLinks() {
        val footerTextView = findViewById<TextView>(R.id.footerLabel)

        val linkNames = resources.getStringArray(R.array.footer_links_names)
        val linkUrls = resources.getStringArray(R.array.footer_links_urls)
        val separator = " • "

        val spannableString = SpannableString(linkNames.joinToString(separator))
        val primaryTextColor = ContextCompat.getColor(this, R.color.primaryText)

        var currentPosition = 0
        for (i in linkNames.indices) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrls[i]))
                    startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    ds.isUnderlineText = false
                    ds.color = primaryTextColor
                }
            }

            val spanEnd = currentPosition + linkNames[i].length
            spannableString.setSpan(
                clickableSpan,
                currentPosition,
                spanEnd,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            currentPosition = spanEnd + separator.length
        }

        footerTextView.text = spannableString
        footerTextView.movementMethod = LinkMovementMethod.getInstance()
    }
}